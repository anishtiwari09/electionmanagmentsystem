package org.day2.electionmanagmentsystem.election.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.day2.electionmanagmentsystem.election.common.helper.ValidElectionDates;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidElectionDates
public class CreateElectionRequest {
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Election name is required")
    private String electionName;
    private Long version;
    @NotNull(message = "Start date and time is required")

    private LocalDateTime startAt;

    @NotNull(message = "End date and time is required")
    private LocalDateTime endAt;
}
