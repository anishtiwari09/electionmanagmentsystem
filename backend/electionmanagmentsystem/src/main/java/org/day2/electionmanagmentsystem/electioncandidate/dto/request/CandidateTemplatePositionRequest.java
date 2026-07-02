package org.day2.electionmanagmentsystem.electioncandidate.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CandidateTemplatePositionRequest {
    @NotNull
    private UUID electionPositionId;

    @Min(1)
    private int numberOfCandidates;
}
