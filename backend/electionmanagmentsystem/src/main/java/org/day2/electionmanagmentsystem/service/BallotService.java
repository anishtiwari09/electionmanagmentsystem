package org.day2.electionmanagmentsystem.service;

import org.day2.electionmanagmentsystem.vote.dto.response.BallotResponse;

import java.util.UUID;

public interface BallotService {
    BallotResponse getBallot(UUID electionPublicId,UUID userPublicId);
}
