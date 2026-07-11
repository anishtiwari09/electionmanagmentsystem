package org.day2.electionmanagmentsystem.voter.service;

import java.util.UUID;

public interface VoterService {
    byte[] generateTemplate(UUID electionId, UUID userId);
}
