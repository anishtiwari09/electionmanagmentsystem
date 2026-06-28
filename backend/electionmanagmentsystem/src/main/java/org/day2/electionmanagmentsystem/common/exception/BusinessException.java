package org.day2.electionmanagmentsystem.common.exception;

import lombok.Getter;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.BaseErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.CandidateErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;

@Getter
public class BusinessException extends RuntimeException{
    private final BaseErrorCode errorCode;

    public BusinessException(BaseErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }

}
