package org.day2.electionmanagmentsystem.election.dto.response;

import lombok.Builder;
import lombok.Data;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.position.dto.response.ElectionPositionResponse;
import org.day2.electionmanagmentsystem.voter.dto.response.ElectionVoterResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder

public class ElectionDetailsResponse {
    private List<ElectionVoterResponse> voters;
    private List <ElectionPositionResponse> positions;
    private UUID electionId;
    private String name;

    private String description;

    private ElectionStatus status;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
