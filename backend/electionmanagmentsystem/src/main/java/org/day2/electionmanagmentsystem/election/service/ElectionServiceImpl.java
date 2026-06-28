package org.day2.electionmanagmentsystem.election.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.dto.request.ChangeElectionStatusRequest;
import org.day2.electionmanagmentsystem.election.dto.request.CreateElectionRequest;
import org.day2.electionmanagmentsystem.election.dto.request.GetElectionsRequest;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionResponse;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionsResponse;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional // Here it is not required as we are only updating single table but may required in futrue
public class ElectionServiceImpl implements ElectionService{
    private final UserRepository userRepository;
    private final ElectionRepository electionRepository;

    private final Election validateAndGetElection(UUID electionPublicId, UUID userPublicId){
        Election election = electionRepository.findByPublicId(electionPublicId).orElseThrow(()-> new BusinessException(ErrorCode.ELECTION_NOT_FOUND));
        if(userPublicId==null) throw new BusinessException(ErrorCode.UNAUTHORIZED);
        if(!election.getUser().getPublicId().equals(userPublicId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return election;

    }
    private void changeElectionStatusToActiveOrScheduled(Election election){
        ElectionStatus currentStatus = election.getStatus();
        LocalDateTime now =LocalDateTime.now();
        LocalDateTime currentStartAt= election.getStartAt();
        LocalDateTime currentEndAt = election.getEndAt();
        if(currentStatus == ElectionStatus.DRAFT || currentStatus == ElectionStatus.HOLD || currentStatus == ElectionStatus.SCHEDULED|| currentStatus == ElectionStatus.ACTIVE){

                    if(currentStartAt==null || currentEndAt==null ){
                        throw new BusinessException(ElectionErrorCode.INVALID_START_OR_END_DATE);
                    }

                    if(currentEndAt.isBefore(now)){
                        throw new BusinessException(ElectionErrorCode.START_END_DATE_PASSED);
                    }
            if (!currentStartAt.isBefore(currentEndAt)) {
                throw new BusinessException(
                        ElectionErrorCode.INVALID_START_OR_END_DATE
                );
            }
            if (!currentStartAt.isAfter(now)) {

                if (currentStatus == ElectionStatus.ACTIVE) {

                    throw new BusinessException(
                            ElectionErrorCode.ALREADY_IN_SAME_STATE
                    );
                }

                election.setStatus(
                        ElectionStatus.ACTIVE
                );

            } else {

                if (currentStatus == ElectionStatus.SCHEDULED) {

                    throw new BusinessException(
                            ElectionErrorCode.ALREADY_IN_SAME_STATE
                    );
                }

                election.setStatus(
                        ElectionStatus.SCHEDULED
                );
            }
                    electionRepository.save(election);

                    return;


        }



throw new BusinessException(ElectionErrorCode.NOT_ALLOWED);



    }
    @Override
    public UUID createNewElection(UUID userPublicId, CreateElectionRequest createElectionRequest) {
        User user = userRepository.findByPublicId(userPublicId).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
//        Todo role check

        String electionName = createElectionRequest.getElectionName();
        if(electionName == null || electionName.isBlank()){
                throw new BusinessException(ErrorCode.INVALID_ELECTION_NAME);
        }

        Election election = new Election();
        String description = createElectionRequest.getDescription();
        election.setDescription(description==null?null:description.trim());
        election.setName(electionName.trim());
        election.setStatus(ElectionStatus.DRAFT);
        election.setUser(user);
        election = electionRepository.save(election);
        return election.getPublicId();
    }
  @Override
    public void changeElectionStatus(UUID userPublicId, ChangeElectionStatusRequest changeElectionStatusRequest){
        UUID electionId = changeElectionStatusRequest.getElectionPublicId();
        Election election = validateAndGetElection(electionId,userPublicId);
        ElectionStatus targetStatus=changeElectionStatusRequest.getElectionStatus();
        switch(targetStatus){
            case ACTIVE -> {
                changeElectionStatusToActiveOrScheduled(election);

            }
            case HOLD -> {
                changeElectionStatusToHold(election);
            }
            case COMPLETED -> {
                changeElectionStatusToCompleted(election);
            }
            case CLOSED -> {
                changeElectionStatusToClosed(election);
            }
            default -> {
                throw new BusinessException(ElectionErrorCode.NOT_ALLOWED);
            }
        }

}

    @Override

    @Transactional(readOnly = true)
    public ElectionsResponse getElections(UUID userPublicId, GetElectionsRequest request) {
        userRepository.findByPublicId(userPublicId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));
        Pageable pageable = PageRequest.of(
               1,
                5,
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

       Page<Election> electionPage;
        if(request.getStatus()!=null && !request.getStatus().isEmpty()){
            electionPage = electionRepository.findByUserPublicIdAndStatusInOrderByUpdatedAtDesc(userPublicId,request.getStatus(),pageable);
        }
        else {
            electionPage = electionRepository.findByUserPublicIdOrderByUpdatedAtDesc(userPublicId,pageable);
        }
        return ElectionsResponse.builder()
                .size(electionPage.getSize())
                .page(electionPage.getNumber())
                .totalPages(electionPage.getTotalPages())
                .totalElement(electionPage.getTotalElements())
                .elections(electionPage.getContent()
                        .stream()
                        .map(this::toElectionResponse)
                        .toList())
                        .build();

    }


    private void changeElectionStatusToHold(Election election) {
        ElectionStatus currentStatus=election.getStatus();
        if(currentStatus == ElectionStatus.HOLD){
            throw new BusinessException(ElectionErrorCode.ALREADY_IN_SAME_STATE);
        }
        if(currentStatus == ElectionStatus.DRAFT || currentStatus == ElectionStatus.ACTIVE || currentStatus == ElectionStatus.SCHEDULED){
            election.setStatus(ElectionStatus.HOLD);
            electionRepository.save(election);
            return;
        }

        throw new BusinessException(ElectionErrorCode.NOT_ALLOWED);
    }
    private void changeElectionStatusToCompleted(Election election){
        ElectionStatus currentStatus=election.getStatus();
        if(currentStatus == ElectionStatus.COMPLETED){
            throw new BusinessException(ElectionErrorCode.ALREADY_IN_SAME_STATE);
        }

        if(currentStatus == ElectionStatus.DRAFT || currentStatus == ElectionStatus.ACTIVE){
            election.setStatus(ElectionStatus.COMPLETED);
            electionRepository.save(election);
            return;
        }

        throw new BusinessException(ElectionErrorCode.NOT_ALLOWED);
    }
    private void changeElectionStatusToClosed(Election election){
        ElectionStatus currentStatus=election.getStatus();
        if(currentStatus == ElectionStatus.CLOSED){
            throw new BusinessException(ElectionErrorCode.ALREADY_IN_SAME_STATE);
        }

        if(currentStatus == ElectionStatus.DRAFT || currentStatus == ElectionStatus.ACTIVE || currentStatus == ElectionStatus.SCHEDULED || currentStatus==ElectionStatus.COMPLETED){
            election.setStatus(ElectionStatus.CLOSED);
            electionRepository.save(election);
            return;
        }

        throw new BusinessException(ElectionErrorCode.NOT_ALLOWED);
    }

    private ElectionResponse toElectionResponse(Election election) {
        return ElectionResponse.builder()
                .publicId(election.getPublicId())
                .name(election.getName())
                .description(election.getDescription())
                .status(election.getStatus())
                .startAt(election.getStartAt())
                .endAt(election.getEndAt())
                .createdAt(election.getCreatedAt())
                .updatedAt(election.getUpdatedAt())
                .build();
    }
}
