package org.day2.electionmanagmentsystem.vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BallotPositionResponse {
    private UUID positionId;
    private String positionName;
    private String description;
    private int minSelection;
    private int maxSelection;
    private List<BallotCandidateResponse> candidates;
}
