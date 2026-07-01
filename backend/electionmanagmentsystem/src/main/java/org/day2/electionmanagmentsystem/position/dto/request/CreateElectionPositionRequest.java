package org.day2.electionmanagmentsystem.position.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.day2.electionmanagmentsystem.position.Helper.ValidateCreateElectionPositionRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateElectionPositionRequest {

    @NotBlank(message = "Position Name is required")
    private String positionName;

    @NotBlank(message = "Description is required")
    private String description;

    private int minSelection;

    @Min(value = 1, message = "Maximum selection must be at least 1")
    private int maxSelection;
}
