package org.day2.electionmanagmentsystem.candidate.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateElectionCandidateRequest {
    @NotNull
private UUID positionPublicId;
    @NotBlank
    @Size(max=100)
private String firstName;
    @NotBlank
    @Size(max=100)
private String lastName;

private String phone;
    @NotBlank
    @Email
    @Size(max = 255)
private String email;
}
