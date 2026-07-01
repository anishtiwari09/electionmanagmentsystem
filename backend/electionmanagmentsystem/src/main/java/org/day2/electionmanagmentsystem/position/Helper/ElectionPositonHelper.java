package org.day2.electionmanagmentsystem.position.Helper;

import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.position.dto.request.CreateElectionPositionRequest;

public class ElectionPositonHelper {
    static public void validateCreatePositionRequest(Election election, CreateElectionPositionRequest request){
        String name = request.getPositionName();
        if(name==null|| name.isBlank())  throw new BusinessException(
                ErrorCode.INVALID_REQUEST
        );
        if (request.getMinSelection() < 0) {

            throw new BusinessException(
                    ErrorCode.INVALID_REQUEST
            );
        }
        if (request.getMaxSelection() <= 0) {

            throw new BusinessException(
                    ErrorCode.INVALID_REQUEST
            );
        }
        if (request.getMaxSelection()
                < request.getMinSelection()) {

            throw new BusinessException(
                    ErrorCode.INVALID_REQUEST
            );
        }
    }
}
