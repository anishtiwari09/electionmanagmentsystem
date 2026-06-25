package org.day2.electionmanagmentsystem.election.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateElectionRequest {
    private String description;
    private String electionName;
    private Long version;
}
