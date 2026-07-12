package org.day2.electionmanagmentsystem.voter.dto.response;

import lombok.Builder;
import lombok.Data;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.enums.VoteStatus;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class VoterElectionsResponse {
    private UUID electionId;

    private String name;

    private String description;

    private ElectionStatus status;
    private LocalDateTime startAt;

    private LocalDateTime endAt;
    private VoteStatus voteStatus;
}
