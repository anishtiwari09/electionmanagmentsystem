package org.day2.electionmanagmentsystem.candidate.service;

import java.util.UUID;

public interface CreateElectionCandidateRequest {
    UUID createCandidate(UUID electionPublicId,
                         UUID userPublicId,
                         CreateElectionCandidateRequest request);

}
