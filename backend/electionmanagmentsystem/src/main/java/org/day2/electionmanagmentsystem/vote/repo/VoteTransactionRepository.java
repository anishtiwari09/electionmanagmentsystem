package org.day2.electionmanagmentsystem.vote.repo;

import org.day2.electionmanagmentsystem.common.enums.VoteStatus;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.vote.VoteTransaction;
import org.day2.electionmanagmentsystem.voter.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteTransactionRepository extends JpaRepository<VoteTransaction,Long> {
    Optional<VoteTransaction> findTopByElectionAndVoterOrderByCreatedAtDesc(Election election, Voter voter);
    Optional<VoteTransaction> findByVoterAndStatus(Voter voter, VoteStatus voteStatus);
    boolean existsByVoterAndStatus(Voter voter, VoteStatus voteStatus);
    Optional<VoteTransaction> findByPublicId(UUID publicId);

}
