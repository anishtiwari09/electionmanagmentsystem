package org.day2.electionmanagmentsystem.common.exception;

import lombok.Getter;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.CandidateErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;
    private final ElectionErrorCode electionErrorCode;
    private final CandidateErrorCode candidateErrorCode;
    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.electionErrorCode = null;
        this.candidateErrorCode = null;
    }
    public BusinessException(ElectionErrorCode errorCode){
        super(errorCode.getMessage());
        this.electionErrorCode = errorCode;
        this.errorCode = null;
        this.candidateErrorCode = null;
    }
    public BusinessException(CandidateErrorCode errorCode){
        this.electionErrorCode = null;
        this.errorCode = null;
        this.candidateErrorCode = errorCode;
    }

}
