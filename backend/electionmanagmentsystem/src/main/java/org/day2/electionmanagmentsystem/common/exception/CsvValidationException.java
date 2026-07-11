package org.day2.electionmanagmentsystem.common.exception;


import lombok.Getter;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.CsvErrorCode;

import java.util.List;
@Getter
public class CsvValidationException extends RuntimeException{
    private final List<CsvValidationError> errors;
    private final CsvErrorCode errorCode;
    public CsvValidationException(List<CsvValidationError> errors) {
        super(CsvErrorCode.INVALID_CSV.getMessage());
       this.errorCode = CsvErrorCode.INVALID_CSV;
        this.errors = errors;
    }

}
