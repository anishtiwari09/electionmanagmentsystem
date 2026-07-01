package org.day2.electionmanagmentsystem.electioncandidate.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class ElectionCandidateResponse {
    private UUID candidateId;
    private UUID userId;
    private String firstName;

    private String lastName;

    private String fullName;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
