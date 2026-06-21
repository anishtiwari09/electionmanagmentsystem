package org.day2.electionmanagmentsystem.vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BallotCandidateResponse {
    private UUID candidateId;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String bio;
    private String manifesto;
    private String groupName;
}
