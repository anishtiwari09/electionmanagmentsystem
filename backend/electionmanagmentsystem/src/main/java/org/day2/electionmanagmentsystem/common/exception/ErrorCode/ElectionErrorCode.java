package org.day2.electionmanagmentsystem.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ElectionErrorCode {
    ALREADY_IN_SAME_STATE("Election State is already in target state"), INVALID_START_OR_END_DATE("Invalid start or end date"), START_AND_END_DATE_MUST_BE_FUTURE("Election start or end date should be future"),
    START_END_DATE_PASSED("Election date has passed now or Election not started yet"), NOT_ALLOWED("Changing Election status is not allowed"),
    INVALID_ACTION_STATE("This action is not allowed in this state");
    final private String message;
}
