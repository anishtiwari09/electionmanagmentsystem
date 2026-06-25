package org.day2.electionmanagmentsystem.vote.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.repo.ElectionPositionRepository;
import org.day2.electionmanagmentsystem.candidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.candidate.repo.ElectionCandidateRepository;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.enums.VoteActionType;
import org.day2.electionmanagmentsystem.common.enums.VoteStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.UserRepository;
import org.day2.electionmanagmentsystem.vote.VoteAction;
import org.day2.electionmanagmentsystem.vote.VoteSelection;
import org.day2.electionmanagmentsystem.vote.VoteTransaction;
import org.day2.electionmanagmentsystem.vote.dto.request.SaveVoteRequest;
import org.day2.electionmanagmentsystem.vote.dto.request.SubmitVoteRequest;
import org.day2.electionmanagmentsystem.vote.repo.VoteActionRepository;
import org.day2.electionmanagmentsystem.vote.repo.VoteSelectionRepository;
import org.day2.electionmanagmentsystem.vote.repo.VoteTransactionRepository;
import org.day2.electionmanagmentsystem.voter.ElectionVoter;
import org.day2.electionmanagmentsystem.voter.repo.ElectionVoterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final ElectionPositionRepository electionPositionRepository;
    private final VoteActionRepository voteActionRepository;
    private List<ElectionCandidate> validateAndLoadCandidate(Election election, SaveVoteRequest saveVoteRequest){
        if(saveVoteRequest==null || saveVoteRequest.getCandidateIds() ==null|| saveVoteRequest.getCandidateIds().isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
        List <UUID> candidateIds = saveVoteRequest.getCandidateIds();
        Set <UUID> uniqueCandidateIds = new HashSet<>(candidateIds);

        if(uniqueCandidateIds.size()!=candidateIds.size()){
            throw new BusinessException(
                    ErrorCode.INVALID_CANDIDATE_SELECTION
            );
        }
        List <ElectionCandidate> candidates = electionCandidateRepository.findByPublicIdIn(candidateIds);

        for(ElectionCandidate candidate : candidates){
            if(candidate.getPosition().getElection().getId().equals(election.getId())){
                throw new BusinessException(
                        ErrorCode.INVALID_CANDIDATE_SELECTION
                );
            }
        }

        Map <ElectionPosition,Long> selectionPerPosition = candidates.stream().collect(Collectors.groupingBy(ElectionCandidate::getPosition,Collectors.counting()));

        List <ElectionPosition> electionPositions = electionPositionRepository.findByElectionOrderByNameAsc(election);

        for(ElectionPosition electionPosition : electionPositions){

            long selectedCount = selectionPerPosition.getOrDefault(electionPosition,0L);
            if(selectedCount < electionPosition.getMinSelection() || selectedCount > electionPosition.getMaxSelection()){
                throw new BusinessException(
                        ErrorCode.INVALID_CANDIDATE_SELECTION
                );
            }

        }
        return candidates;
    }
    private void validateVersion(VoteTransaction voteTransaction, SaveVoteRequest saveVoteRequest){
        if(voteTransaction.getVersion()==null){
            return;
        }

        if(voteTransaction.getVersion().equals(saveVoteRequest.getVersion())){
            return;
        }
        throw new BusinessException(ErrorCode.INVALID_REQUEST);
    }
    private void saveVoteAction(VoteTransaction voteTransaction, User user, VoteActionType voteActionType){
        VoteAction voteAction = new VoteAction();
        voteAction.setPerformedBy(user);
        voteAction.setVoteTransaction(voteTransaction);
        voteAction.setActionType(voteActionType);
        voteAction.setPerformedAt(LocalDateTime.now());
         voteActionRepository.save(voteAction);
    }

    private void saveVoteSelection(VoteTransaction voteTransaction, List <ElectionCandidate> electionCandidates){

        voteSelectionRepository.deleteByVoteTransaction(
                voteTransaction
        );
        List <VoteSelection> voteSelections = electionCandidates.stream().map(candidate ->{
           VoteSelection voteSelection = new VoteSelection();
           voteSelection.setCandidate(candidate);
           voteSelection.setVoteTransaction(voteTransaction);
           return voteSelection;
        }).toList();

        voteSelectionRepository.saveAll(voteSelections);
    }

    private void validateElectionVotingAllowed(Election election){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startAt=election.getStartAt();
        LocalDateTime endAt = election.getEndAt();
        ElectionStatus electionStatus = election.getStatus();
        if(election.getStatus()== ElectionStatus.ACTIVE&&startAt!=null&&!now.isBefore(election.getStartAt())&&endAt!=null&&now.isAfter(endAt)){
            return;
        }

        if(electionStatus==ElectionStatus.ACTIVE&&(startAt ==null ||endAt ==null)){
            throw new BusinessException(ErrorCode.INVALID_START_OR_END_DATE);
        }

        throw new BusinessException(ErrorCode.INVALID_ELECTION_STATE);
    }
    @Override
    public void saveDraft(UUID electionPublicId, UUID userPublicId, SaveVoteRequest saveVoteRequest){
        Election election = electionRepository.findByPublicId(electionPublicId).orElseThrow(()-> new BusinessException(ErrorCode.ELECTION_NOT_FOUND));

        // check election state
        validateElectionVotingAllowed(election);
        List <ElectionCandidate> candidates =validateAndLoadCandidate(election,saveVoteRequest);

        User user = userRepository.findByPublicId(userPublicId).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
        ElectionVoter electionVoter = electionVoterRepository.findByElectionAndUser(election, user).orElseThrow(()-> new BusinessException(ErrorCode.VOTER_NOT_ELIGIBLE));
        VoteTransaction latestVoteTransaction=voteTransactionRepository.findTopByElectionAndElectionVoterOrderByCreatedAtDesc(election,electionVoter).orElse(null);
        VoteStatus currentVoteStatus=latestVoteTransaction!=null ? latestVoteTransaction.getStatus():null;



        if(latestVoteTransaction == null || currentVoteStatus==VoteStatus.REVOKED_FOR_REVOTE || currentVoteStatus == VoteStatus.DRAFT){


            VoteTransaction voteTransaction = new VoteTransaction();

            if(currentVoteStatus==VoteStatus.DRAFT)
            {
                validateVersion(voteTransaction,saveVoteRequest);
                voteTransaction = latestVoteTransaction;
            }

            voteTransaction.setElection(election);
            voteTransaction.setElectionVoter(electionVoter);
            voteTransaction.setStatus(VoteStatus.DRAFT);
            voteTransaction = voteTransactionRepository.save(voteTransaction);
            saveVoteSelection(voteTransaction,candidates);
            VoteActionType voteActiontype = VoteActionType.DRAFT_CREATED;
            if(currentVoteStatus==VoteStatus.DRAFT)
            {
                voteActiontype = VoteActionType.DRAFT_UPDATED;
            }
            saveVoteAction(voteTransaction,user,voteActiontype);

       }
       else if (latestVoteTransaction.getStatus()==VoteStatus.SUBMITTED){
           throw new BusinessException(ErrorCode.VOTE_ALREADY_SUBMITTED);
       } else if (currentVoteStatus== VoteStatus.PENDING_VERIFICATION) {
           throw new BusinessException(ErrorCode.VOTE_ALREADY_SUBMITTED);
       }
       else {
           throw new BusinessException(ErrorCode.NOT_ALLOWED_REVOTING);
       }
    }
    @Override
    public void submitVote(UUID electionPublicId, UUID userPublicId, SubmitVoteRequest submitVoteRequest){
        Election election = electionRepository.findByPublicId(electionPublicId).orElseThrow(()-> new BusinessException(ErrorCode.ELECTION_NOT_FOUND));
        validateElectionVotingAllowed(election);

        User user = userRepository
                .findByPublicId(
                        userPublicId
                )
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.USER_NOT_FOUND
                        )
                );
        VoteTransaction voteTransaction =
                voteTransactionRepository
                        .findByPublicId(
                                submitVoteRequest.getVoteTransactionPublicId()
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.VOTE_NOT_FOUND
                                )
                        );
        ElectionVoter electionVoter =
                electionVoterRepository
                        .findByElectionAndUser(
                                voteTransaction.getElection(),
                                user
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.VOTER_NOT_ELIGIBLE
                                )
                        );
        SaveVoteRequest voteRequest =
                SaveVoteRequest.
                        builder().version(submitVoteRequest.getVersion()).build();
        validateVersion(voteTransaction,voteRequest);
        if (!voteTransaction.getElectionVoter()
                .getId()
                .equals(electionVoter.getId())) {

            throw new BusinessException(
                    ErrorCode.UNAUTHORIZED
            );
        }

        if(voteTransaction.getStatus() == VoteStatus.DRAFT){
            voteTransaction.setStatus(VoteStatus.PENDING_VERIFICATION);
            voteTransaction.setSubmittedAt( LocalDateTime.now());
            voteTransactionRepository.save(
                    voteTransaction
            );
            saveVoteAction(
                    voteTransaction,
                    user,
                    VoteActionType.SUBMISSION_REQUESTED
            );

        }

       throw new BusinessException(ErrorCode.INVALID_CANDIDATE_SELECTION);
    }

}
