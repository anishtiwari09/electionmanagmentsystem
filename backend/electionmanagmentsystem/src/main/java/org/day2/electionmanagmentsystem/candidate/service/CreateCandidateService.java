package org.day2.electionmanagmentsystem.candidate.service;

import org.day2.electionmanagmentsystem.candidate.dto.request.CreateElectionCandidateRequest;

import java.util.UUID;

public interface CreateCandidateService {
    UUID createCandidate(UUID electionPublicId,
                         UUID userPublicId,
                         CreateElectionCandidateRequest request);

}
