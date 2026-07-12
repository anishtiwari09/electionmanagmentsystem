package org.day2.electionmanagmentsystem.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.repo.ElectionPositionRepository;
import org.day2.electionmanagmentsystem.electioncandidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.electioncandidate.repo.ElectionCandidateRepository;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.repo.UserRepository;
import org.day2.electionmanagmentsystem.vote.VoteTransaction;
import org.day2.electionmanagmentsystem.vote.repo.VoteTransactionRepository;
import org.day2.electionmanagmentsystem.vote.dto.response.BallotCandidateResponse;
import org.day2.electionmanagmentsystem.vote.dto.response.BallotPositionResponse;
import org.day2.electionmanagmentsystem.vote.dto.response.BallotResponse;
import org.day2.electionmanagmentsystem.voter.Voter;
import org.day2.electionmanagmentsystem.voter.repo.VoterRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BallotServiceImpl implements BallotService{
    private final ElectionRepository electionRepository;
    private final VoterRepository voterRepository;
    private final UserRepository userRepository;
    private final VoteTransactionRepository voteTransactionRepository;
    private final ElectionPositionRepository electionPositionRepository;
    private final ElectionCandidateRepository electionCandidateRepository;

    @Override
    public BallotResponse getBallot(UUID electionPublicId, UUID userPublicId){

        Election election = electionRepository
                .findByPublicId(electionPublicId)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.ELECTION_NOT_FOUND)
                );
        User user = userRepository.findByPublicId(userPublicId).orElseThrow(() -> new RuntimeException("User not found"));

        Voter voter = voterRepository.findByElectionAndUser(election,user).orElseThrow(()-> new RuntimeException("Voter is not eligible for this election"));
        Optional<VoteTransaction> latestVoteOptional = voteTransactionRepository.findTopByElectionAndVoterOrderByCreatedAtDesc(election,voter);
        if(latestVoteOptional.isPresent()){
            VoteTransaction voteStatus=latestVoteOptional.get();
            switch(voteStatus.getStatus()){

                case SUBMITTED:
                    throw new RuntimeException(
                            "Vote has already been submitted"
                    );

                case PENDING_VERIFICATION:
                    throw new RuntimeException("Vote verification is in progress");

                case DRAFT:
                case REVOKED_FOR_REVOTE:
                    break;

            }
        }

        List <ElectionPosition> positions =electionPositionRepository.findByElectionOrderByNameAsc(election);

//        List <ElectionCandidate> candidates =electionCandidateRepository.findByPositionInOrderByCreatedAtAsc(positions);

//        Map<ElectionPosition,List<ElectionCandidate>> candidatePosition = candidates.stream().collect(Collectors.groupingBy(ElectionCandidate::getPosition));


        List <BallotPositionResponse> ballotPositionsResponse = new ArrayList<>();


        for(ElectionPosition position : positions){
            List <BallotCandidateResponse> candidateResponses = new ArrayList<>();
            List <ElectionCandidate> candidateList = electionCandidateRepository.findByPositionOrderByCreatedAtAsc(position);
            for(ElectionCandidate candidate : candidateList){
                BallotCandidateResponse ballotCandidateResponse =
                        BallotCandidateResponse.builder()
                                .candidateId(candidate.getPublicId())
                                .firstName(candidate.getUser().getFirstName())
                                .lastName(candidate.getUser().getLastName())
                                .photoUrl(null)
                                .build();
                candidateResponses.add(ballotCandidateResponse);


            }

            BallotPositionResponse ballotPositionResponse =
                    BallotPositionResponse.builder()
                            .positionId(position.getPublicId())
                            .positionName(position.getName())
                            .description(position.getDescription())
                            .minSelection(position.getMinSelection())
                            .maxSelection(position.getMaxSelection())
                            .candidates(candidateResponses)
                            .build();

            ballotPositionsResponse.add(ballotPositionResponse);
        }


        return BallotResponse.builder().
                electionId(election.getPublicId())
                .startAt(election.getStartAt())
                .endAt(election.getEndAt())
                .positions(ballotPositionsResponse)
                .build();

    }




}



