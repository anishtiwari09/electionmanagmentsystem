package org.day2.electionmanagmentsystem.election.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionDetailsResponse;
import org.day2.electionmanagmentsystem.election.mapper.ElectionMapper;
import org.day2.electionmanagmentsystem.electioncandidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.electioncandidate.repo.ElectionCandidateRepository;
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
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.dto.response.ElectionPositionResponse;
import org.day2.electionmanagmentsystem.position.mapper.ElectionPositionMapper;
import org.day2.electionmanagmentsystem.position.repo.ElectionPositionRepository;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.repo.UserRepository;
import org.day2.electionmanagmentsystem.voter.Voter;
import org.day2.electionmanagmentsystem.voter.dto.response.ElectionVoterResponse;
import org.day2.electionmanagmentsystem.voter.mapper.ElectionVoterMapper;
import org.day2.electionmanagmentsystem.voter.repo.VoterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final VoterRepository voterRepository;

    private final ElectionCandidateRepository electionCandidateRepository;
    private final ElectionPositionRepository electionPositionRepository;
    private final ElectionPositionMapper electionPositionMapper;
   private final ElectionMapper electionMapper;
    private final ElectionVoterMapper electionVoterMapper;

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
    public ElectionResponse createNewElection(UUID userPublicId, CreateElectionRequest createElectionRequest) {
        User user = userRepository.findByPublicId(userPublicId).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
//        Todo role check

        String electionName = createElectionRequest.getElectionName();


        Election election = new Election();
        String description = createElectionRequest.getDescription();
        election.setDescription(description.trim());
        election.setName(electionName.trim().toLowerCase());
        election.setStatus(ElectionStatus.DRAFT);
        election.setUser(user);
        election.setStartAt(createElectionRequest.getStartAt());
        election.setEndAt(createElectionRequest.getEndAt());
        election = electionRepository.save(election);
        return electionMapper.toElectionResponse(election);


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
               0,
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
                        .map(electionMapper::toElectionResponse)
                        .toList())
                        .build();

    }

    @Override
    public ElectionDetailsResponse getElectionDetails(UUID electionId, UUID userId) {
        Election election = electionRepository.findByPublicIdAndUserPublicId(electionId,userId).orElseThrow(()-> new BusinessException(ErrorCode.ELECTION_NOT_FOUND));
        List<Voter> voters = voterRepository.findByElection(election);
        List <ElectionPosition> electionPositions= electionPositionRepository.findByElectionOrderByNameAsc(election);
        List <ElectionVoterResponse> electionVoterResponses = electionVoterMapper.toResponses(voters);
        List <ElectionCandidate> electionCandidates=null;
        if(!electionPositions.isEmpty())
            electionCandidates =electionCandidateRepository.findByPositionIn(electionPositions);

        List <ElectionPositionResponse> electionPositionResponseList= electionPositionMapper.toResponses(electionPositions,electionCandidates);
        return electionMapper.toDetailsResponse(election,electionPositionResponseList,electionVoterResponses);
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



}
