package org.day2.electionmanagmentsystem.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ElectionErrorCode implements BaseErrorCode{
    ALREADY_IN_SAME_STATE("Election State is already in target state"),
    START_END_DATE_PASSED("Election date has passed now or Election not started yet"), NOT_ALLOWED("Changing Election status is not allowed"),
    INVALID_ACTION_STATE("This action is not allowed in this state"),
    START_AND_END_DATE_MUST_BE_FUTURE("Election start and end dates must be in the future"),
    INVALID_START_OR_END_DATE("End date must be after start date");
    final private String message;
}
