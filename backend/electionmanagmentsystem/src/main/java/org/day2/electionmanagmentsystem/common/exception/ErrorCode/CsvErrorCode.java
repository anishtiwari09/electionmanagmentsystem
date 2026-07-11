package org.day2.electionmanagmentsystem.common.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CsvErrorCode implements BaseErrorCode{
    INVALID_CSV(
            "INVALID_CSV",
            "CSV validation failed."
    );
    private final String code;
    private final String message;
}
