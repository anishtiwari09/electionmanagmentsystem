package org.day2.electionmanagmentsystem.common.exception;


import lombok.Getter;

import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum ErrorCode {
    ElECTION_NOT_FOUND("Election not found"),
    USER_NOT_FOUND("User not found"),
    VOTER_NOT_ELIGIBLE("Voter is not eligible for vote"),
    VOTE_ALREADY_SUBMITTED("Vote is already submitted"),
    VOTE_VERIFICATION_IN_PROGRESS("Vote verification in progress"),
    VOTE_INVALIDATED("Vote has been invalidated"),


//    Global Error
    INVALID_REQUEST("Invalid Request parameter"),
    VALIDATION_FAILED(
            "Validation failed"
    ),
    INVALID_REQUEST_HEADER("Invalid Request Header"),
    INTERNAL_SERVER_ERROR("Internal Server Error");

    private final String message;

}
