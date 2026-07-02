package org.day2.electionmanagmentsystem.electioncandidate.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GenerateCandidateTemplateRequest {
    private UUID electionId;
    @Valid
    @NotEmpty
    private List<CandidateTemplatePositionRequest> positions;
}
