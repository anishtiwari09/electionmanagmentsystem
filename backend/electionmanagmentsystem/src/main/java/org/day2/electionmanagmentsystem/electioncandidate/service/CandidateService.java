package org.day2.electionmanagmentsystem.electioncandidate.service;

import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CreateElectionCandidateRequest;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.GenerateCandidateTemplateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface CandidateService {
    UUID createCandidate(UUID electionPublicId,
                         UUID userPublicId,
                         CreateElectionCandidateRequest request);
    byte[] generateTemplate(UUID userId, GenerateCandidateTemplateRequest request);

    public void uploadCandidates(UUID electionId, UUID userPublicId, MultipartFile file);
}
