package org.day2.electionmanagmentsystem.candidate.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class createElectionCandidateRequest {
private UUID positionPublicId;
private String firstName;
private String lastName;
private String phone;
private String email;
}
