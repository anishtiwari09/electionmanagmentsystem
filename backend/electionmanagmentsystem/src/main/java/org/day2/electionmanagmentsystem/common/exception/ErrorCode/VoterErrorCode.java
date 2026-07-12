package org.day2.electionmanagmentsystem.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum VoterErrorCode implements BaseErrorCode{
    NON_EMPTY_VOTER_LIST("Voter list is already exists");
    final private String message;

}
