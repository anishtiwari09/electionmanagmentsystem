package org.day2.electionmanagmentsystem.candidate.helper;

import org.day2.electionmanagmentsystem.candidate.dto.request.CreateElectionCandidateRequest;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.CandidateErrorCode;

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
    }
}
