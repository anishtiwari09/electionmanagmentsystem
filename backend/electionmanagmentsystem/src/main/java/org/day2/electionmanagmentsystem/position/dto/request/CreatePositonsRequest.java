package org.day2.electionmanagmentsystem.position.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.day2.electionmanagmentsystem.position.Helper.ValidateCreateElectionPositionRequest;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePositonsRequest {
    @Valid
    @ValidateCreateElectionPositionRequest
    @NotEmpty(message = "Request payload should not be empty")
    List<CreateElectionPositionRequest> positions;
}
