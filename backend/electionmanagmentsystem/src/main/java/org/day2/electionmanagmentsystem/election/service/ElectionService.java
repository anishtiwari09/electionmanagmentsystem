package org.day2.electionmanagmentsystem.election.service;

import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.dto.request.CreateElectionRequest;
import org.day2.electionmanagmentsystem.election.dto.request.ChangeElectionStatusRequest;

import java.util.UUID;

public interface ElectionService {
    UUID createNewElection(UUID userPublicId, CreateElectionRequest request);
    void changeElectionStatus(UUID userPublicId, ChangeElectionStatusRequest electionRequest);
}
