package org.day2.electionmanagmentsystem.vote.service;


import org.day2.electionmanagmentsystem.vote.dto.response.SaveVoteRequest;

import java.util.UUID;

public interface VoteService {
   void saveDraft(UUID electionPublicId, UUID userPublicId, SaveVoteRequest saveVoteRequest);
}
