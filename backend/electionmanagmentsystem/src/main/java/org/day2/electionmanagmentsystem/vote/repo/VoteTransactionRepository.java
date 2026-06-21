package org.day2.electionmanagmentsystem.vote.repo;

import org.day2.electionmanagmentsystem.common.enums.VoteStatus;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.vote.VoteTransaction;
import org.day2.electionmanagmentsystem.voter.ElectionVoter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VoteTransactionRepository extends JpaRepository<VoteTransaction,Long> {
    Optional<VoteTransaction> findTopByElectionAndElectionVoterOrderByCreatedAtDesc(Election election, ElectionVoter electionVoter);
    Optional<VoteTransaction> findByElectionVoterAndStatus(ElectionVoter electionVoter, VoteStatus voteStatus);
    boolean existsByElectionVoterAndStatus(ElectionVoter electionVoter, VoteStatus voteStatus);

}
