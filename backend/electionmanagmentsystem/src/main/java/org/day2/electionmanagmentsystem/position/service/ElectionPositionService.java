package org.day2.electionmanagmentsystem.position.service;

import org.day2.electionmanagmentsystem.position.dto.request.CreateElectionPositionRequest;
import org.day2.electionmanagmentsystem.position.dto.request.CreatePositonsRequest;

import java.util.UUID;

public interface ElectionPositionService {
    UUID createPositions(UUID electionPublicId, UUID userPublicId, CreatePositonsRequest requests);
}
