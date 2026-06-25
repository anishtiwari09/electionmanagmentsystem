package org.day2.electionmanagmentsystem.common.exception.ErrorCode;


import lombok.Getter;

import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum ErrorCode {
    ELECTION_NOT_FOUND("Election not found"),
    USER_NOT_FOUND("User not found"),
    VOTER_NOT_ELIGIBLE("Voter is not eligible for vote"),
    VOTE_ALREADY_SUBMITTED("Vote is already submitted"),
    VOTE_VERIFICATION_IN_PROGRESS("Vote verification in progress"),
    VOTE_INVALIDATED("Vote has been invalidated"),
    NOT_ALLOWED_REVOTING("Revoting is not allowed please contact to administrator"),

//    Global Error
    INVALID_REQUEST("Invalid Request parameter"),
    VALIDATION_FAILED(
            "Validation failed"
    ),
    VOTE_NOT_FOUND("Invalid Transaction Id"),
    INVALID_ELECTION_STATE("Election is either closed or completed"),
    INVALID_START_OR_END_DATE("Start or end date is not set"),
    STALE_BALLOT("Your ballet has changed, Please refresh and try again."),
    INVALID_REQUEST_HEADER("Invalid Request Header"),
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    INVALID_CANDIDATE_SELECTION(
        "Invalid candidate selection"
    ), UNAUTHORIZED("Unauthorized"), INVALID_ELECTION_NAME("Election name is required");
    private final String message;

}
