package org.day2.electionmanagmentsystem.common.exception.ErrorCode;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CandidateErrorCode {
    CANDIDATE_ALREADY_EXISTS("Candidate is already registered with that position"),
    INVALID_POSITION("Invalid position selection"), INVALID_FIRST_NAME_OR_LAST_NAME("first name or/and last name should not be blank");
    final private String message;
}
