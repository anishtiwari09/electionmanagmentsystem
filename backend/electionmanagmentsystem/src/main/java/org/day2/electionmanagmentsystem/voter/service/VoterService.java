package org.day2.electionmanagmentsystem.voter.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VoterService {
    public byte[] generateTemplate(UUID electionId, UUID userId);
    public void uploadVoters(UUID electionId, UUID userId, MultipartFile file);


}
