package org.day2.electionmanagmentsystem.vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallotResponse {
    private UUID electionId;
    private String electionName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private List<BallotPositionResponse> positions;
}
