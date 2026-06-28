package org.day2.electionmanagmentsystem.election.dto.response;

import lombok.Builder;
import lombok.Data;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
@Data
public class ElectionResponse {
    private UUID electionId;

    private String name;

    private String description;

    private ElectionStatus status;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
