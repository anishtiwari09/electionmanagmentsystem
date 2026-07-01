package org.day2.electionmanagmentsystem.common.exception.ErrorCode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum PositionErrorCode implements BaseErrorCode{
    PositionErrorCode("Invalid "),
    INVALID_MIN_MAX_SELECTION("Invalid min or max selection / max selection should not be smaller than min selection"),
    INVALID_ELECTION_POSITION_NAME("Election position should not be empty or invalid only alphabet and number is allowed."),
    DUPLICATE_POSITION_NAME("Duplicate entry is not allowed."), POSITION_ALREADY_EXISTS("Position Already exits ");
    final private String message;
}
