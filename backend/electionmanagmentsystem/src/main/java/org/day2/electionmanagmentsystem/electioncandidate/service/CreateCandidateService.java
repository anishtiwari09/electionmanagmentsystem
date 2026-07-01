package org.day2.electionmanagmentsystem.electioncandidate.service;

import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CreateElectionCandidateRequest;

import java.util.UUID;

public interface CreateCandidateService {
    UUID createCandidate(UUID electionPublicId,
                         UUID userPublicId,
                         CreateElectionCandidateRequest request);

}
