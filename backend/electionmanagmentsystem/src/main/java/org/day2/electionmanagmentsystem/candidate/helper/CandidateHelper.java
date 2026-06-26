package org.day2.electionmanagmentsystem.candidate.helper;

import org.day2.electionmanagmentsystem.candidate.dto.request.CreateElectionCandidateRequest;
import org.day2.electionmanagmentsystem.candidate.repo.ElectionCandidateRepository;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.CandidateErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.repo.ElectionPositionRepository;
import org.day2.electionmanagmentsystem.user.User;

import java.util.UUID;

public class CandidateHelper {
    public static void validateCreateCandidateRequest(CreateElectionCandidateRequest request){
        if (request.getPositionPublicId() == null) {
            throw new BusinessException(
                    CandidateErrorCode.INVALID_POSITION
            );
        }

        if(request.getFirstName() == null || request.getFirstName().isBlank() || request.getLastName() == null || request.getLastName().isBlank()){
            throw new BusinessException(
                    CandidateErrorCode.INVALID_FIRST_NAME_OR_LAST_NAME
            );
        }
//     if(request.getEmail()==null||request.getEmail().isBlank()||)
    }
    public static ElectionPosition getElectionPosition(
            ElectionPositionRepository electionPositionRepository,
            Election election,
            UUID positionPublicId
    ) {

        return electionPositionRepository.findByPublicIdAndElectionPublicId(positionPublicId,election.getPublicId()).orElseThrow(()-> new BusinessException(ErrorCode.INVALID_REQUEST));

    }
    public static void validateDuplicateCandidate(
            ElectionCandidateRepository electionCandidateRepository,
            ElectionPosition position,
            User user
    ) {

        if (electionCandidateRepository.existsByPositionAndUser(position,
                user
        )) {

            throw new BusinessException(
                    CandidateErrorCode.CANDIDATE_ALREADY_EXISTS
            );
        }
    }
}
