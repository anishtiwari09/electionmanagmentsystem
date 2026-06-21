package org.day2.electionmanagmentsystem.common.exception;

import lombok.*;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private String code;
    private String message;
    private LocalDateTime timeStamp;
}
