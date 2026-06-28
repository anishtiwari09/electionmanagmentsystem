package org.day2.electionmanagmentsystem.common.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private String code;
    private String message;
    private boolean success;
}
