package org.day2.electionmanagmentsystem.vote.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.candidate.ElectionCandidateRepository;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.ElectionRepository;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.UserRepository;
import org.day2.electionmanagmentsystem.vote.dto.response.SaveVoteRequest;
import org.day2.electionmanagmentsystem.vote.repo.VoteSelectionRepository;
import org.day2.electionmanagmentsystem.vote.repo.VoteTransactionRepository;
import org.day2.electionmanagmentsystem.voter.ElectionVoter;
import org.day2.electionmanagmentsystem.voter.ElectionVoterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteServiceImpl implements VoteService{
    private final ElectionRepository electionRepository;
    private final UserRepository userRepository;
    private final ElectionVoterRepository electionVoterRepository;
    private final VoteTransactionRepository voteTransactionRepository;
    private final VoteSelectionRepository voteSelectionRepository;
    private final ElectionCandidateRepository electionCandidateRepository;

    @Override

    public void saveDraft(UUID electionPublicId, UUID userPublicId, SaveVoteRequest saveVoteRequest){
        Election election = electionRepository.findByPublicId(electionPublicId).orElseThrow(()-> new BusinessException(ErrorCode.ElECTION_NOT_FOUND));
        User user = userRepository.findByPublicId(userPublicId).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ElectionVoter electionVoter = electionVoterRepository.findByElectionAndUser(election, user).orElseThrow(()-> new BusinessException(ErrorCode.VOTER_NOT_ELIGIBLE));

    }
}
