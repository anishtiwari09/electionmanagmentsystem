package org.day2.electionmanagmentsystem.common.helper;

import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;

public class HelperLogic {
    static void validateVersion(Long sourceVersion, Long targetVersion){
        if(targetVersion == null) return;

        if(targetVersion.equals(sourceVersion) ) return;

        throw new BusinessException(ErrorCode.INVALID_REQUEST);


    }
}
