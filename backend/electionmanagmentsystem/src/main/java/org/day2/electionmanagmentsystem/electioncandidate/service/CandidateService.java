package org.day2.electionmanagmentsystem.electioncandidate.service;

import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CreateElectionCandidateRequest;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.GenerateCandidateTemplateRequest;

import java.util.UUID;

public interface CandidateService {
    UUID createCandidate(UUID electionPublicId,
                         UUID userPublicId,
                         CreateElectionCandidateRequest request);
    byte[] generateTemplate(UUID userId, GenerateCandidateTemplateRequest request);
}
