package org.day2.electionmanagmentsystem.position.dto.response;

import lombok.Builder;
import lombok.Data;
import org.day2.electionmanagmentsystem.electioncandidate.dto.response.ElectionCandidateResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ElectionPositionResponse {
    private UUID electionPositionId;
    private String positionName;
    private Integer minSelection;
    private Integer maxSelection;
    private List <ElectionCandidateResponse> candidates;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
