package org.day2.electionmanagmentsystem.common.exception.ErrorCode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum PositionErrorCode {
    PositionErrorCode("Invalid ");
    final private String message;
}
