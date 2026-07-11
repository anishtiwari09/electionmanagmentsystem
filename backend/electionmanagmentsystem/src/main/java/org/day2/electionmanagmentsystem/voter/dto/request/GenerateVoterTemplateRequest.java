package org.day2.electionmanagmentsystem.voter.dto.request;

import lombok.Getter;

import java.util.UUID;
@Getter
public class GenerateVoterTemplateRequest {
    private UUID electionId;
}
