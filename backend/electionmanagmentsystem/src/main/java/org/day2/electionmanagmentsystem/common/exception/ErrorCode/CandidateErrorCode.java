package org.day2.electionmanagmentsystem.common.exception.ErrorCode;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CandidateErrorCode implements BaseErrorCode{
    CANDIDATE_ALREADY_EXISTS("Candidate is already registered with that position"),
    INVALID_POSITION("Invalid position selection"), INVALID_FIRST_NAME_OR_LAST_NAME("first name or/and last name should not be blank"),INVALID_CANDIDATE_COUNT("Invalid candidate count or requested candidate count should be equal or more than maxSelection"), INVALID_CANDIDATE_UPLOAD_ROW("Invalid candidate row found please remove it and upload again"), CANDIDATES_ALREADY_EXIST("candidate list is not empty");
    
    final private String message;
}
