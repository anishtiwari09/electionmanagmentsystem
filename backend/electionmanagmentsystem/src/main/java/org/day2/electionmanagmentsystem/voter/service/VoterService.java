package org.day2.electionmanagmentsystem.voter.service;

import org.day2.electionmanagmentsystem.election.dto.request.GetElectionsRequest;
import org.day2.electionmanagmentsystem.voter.dto.response.VoterElectionsResponses;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VoterService {
    public byte[] generateTemplate(UUID electionId, UUID userId);
    public void uploadVoters(UUID electionId, UUID userId, MultipartFile file);
    public VoterElectionsResponses getElections(UUID userId);

}
