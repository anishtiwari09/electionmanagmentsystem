package org.day2.electionmanagmentsystem.vote.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveVoteRequest {
    private List<UUID> candidateIds;
    private Long version;
}
