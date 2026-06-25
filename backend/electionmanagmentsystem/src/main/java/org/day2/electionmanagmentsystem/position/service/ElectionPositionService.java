package org.day2.electionmanagmentsystem.position.service;

import org.day2.electionmanagmentsystem.position.dto.request.CreateElectionPositionRequest;

import java.util.UUID;

public interface ElectionPositionService {
    UUID createPosition(UUID electionPublicId, UUID userPublicId, CreateElectionPositionRequest request);
}
