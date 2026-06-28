package org.day2.electionmanagmentsystem.election.service;

import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.dto.request.CreateElectionRequest;
import org.day2.electionmanagmentsystem.election.dto.request.ChangeElectionStatusRequest;
import org.day2.electionmanagmentsystem.election.dto.request.GetElectionsRequest;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionsResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public interface ElectionService {
    UUID createNewElection(UUID userPublicId, CreateElectionRequest request);
    void changeElectionStatus(UUID userPublicId, ChangeElectionStatusRequest electionRequest);
    ElectionsResponse getElections(
            UUID userPublicId,
            GetElectionsRequest request
    );
}
