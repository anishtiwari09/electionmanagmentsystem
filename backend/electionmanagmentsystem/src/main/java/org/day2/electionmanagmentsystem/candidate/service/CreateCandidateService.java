package org.day2.electionmanagmentsystem.candidate.service;

import java.util.UUID;

public interface CreateCandidateService {
    UUID createCandidate(UUID electionPublicId,
                         UUID userPublicId,
                         CreateCandidateService request);

}
