package org.day2.electionmanagmentsystem.common.exception.ErrorCode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum PositionErrorCode implements BaseErrorCode{
    PositionErrorCode("Invalid "),
    INVALID_MIN_MAX_SELECTION("Invalid min or max selection / max selection should not be smaller than min selection"),
    INVALID_ELECTION_POSITION_NAME("Election position should not be empty or invalid only alphabet and number is allowed."),
    DUPLICATE_POSITION_NAME("Duplicate entry is not allowed."), POSITION_ALREADY_EXISTS("Position Already exits "), NO_POSITION_FOUND("No positions found corresponding to that election please  add position list first.") ,INVALID_POSITION_REQUEST("Please add valid position request "), POSITION_NOT_FOUND("Invalid Position Id");
    final private String message;
}
