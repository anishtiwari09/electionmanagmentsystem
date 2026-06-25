package org.day2.electionmanagmentsystem.position.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.Helper.ElectionPositonHelper;
import org.day2.electionmanagmentsystem.position.dto.request.CreateElectionPositionRequest;
import org.day2.electionmanagmentsystem.position.repo.ElectionPositionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ElectionPositionServiceImpl implements ElectionPositionService{

    private final ElectionRepository electionRepository;
    private final ElectionPositionRepository electionPositionRepository;
    @Override
    public UUID createPosition(UUID electionPublicId, UUID userPublicId, CreateElectionPositionRequest request) {
       Election election = electionRepository.findByPublicIdAndUserPublicId(electionPublicId,userPublicId).orElseThrow(()-> new BusinessException(ErrorCode.ELECTION_NOT_FOUND));


       if(election.getStatus()!= ElectionStatus.DRAFT){
           throw new BusinessException(
                   ElectionErrorCode.INVALID_ACTION_STATE
           );
       }
        ElectionPositonHelper.validateCreatePositionRequest(election,request);
        ElectionPosition electionPosition = new ElectionPosition();
        electionPosition.setElection(election);
        electionPosition.setName(request.getName().trim().toLowerCase());
        electionPosition.setDescription( request.getDescription() == null
                ? null
                : request.getDescription().trim());
        electionPosition.setMinSelection(request.getMinSelection());
        electionPosition.setMaxSelection(request.getMaxSelection());

        electionPosition = electionPositionRepository.save(electionPosition);
        return electionPosition.getPublicId();
    }
}
