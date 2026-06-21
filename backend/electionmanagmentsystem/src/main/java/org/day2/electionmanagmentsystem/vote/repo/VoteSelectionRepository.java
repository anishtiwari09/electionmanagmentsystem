package org.day2.electionmanagmentsystem.vote.repo;

import org.day2.electionmanagmentsystem.vote.VoteSelection;
import org.day2.electionmanagmentsystem.vote.VoteTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VoteSelectionRepository extends JpaRepository<VoteSelection,Long> {
    List <VoteSelection> findByVoteTransaction(VoteTransaction voteTransaction);
    void deleteByVoteTransaction(VoteTransaction voteTransaction);
}
