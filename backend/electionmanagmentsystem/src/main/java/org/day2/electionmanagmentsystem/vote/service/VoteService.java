package org.day2.electionmanagmentsystem.vote.service;


import org.day2.electionmanagmentsystem.vote.dto.request.SaveVoteRequest;
import org.day2.electionmanagmentsystem.vote.dto.request.SubmitVoteRequest;

import java.util.UUID;

public interface VoteService {
   void saveDraft(UUID electionPublicId, UUID userPublicId, SaveVoteRequest saveVoteRequest);
   void submitVote(UUID electionPublicId, UUID userPublicId, SubmitVoteRequest submitVoteRequest);
}
