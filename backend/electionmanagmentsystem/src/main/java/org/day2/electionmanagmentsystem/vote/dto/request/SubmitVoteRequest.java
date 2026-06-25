package org.day2.electionmanagmentsystem.vote.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class SubmitVoteRequest {
    private Long version;
    private UUID voteTransactionPublicId;
}
