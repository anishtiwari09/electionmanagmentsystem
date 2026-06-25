package org.day2.electionmanagmentsystem.vote.repo;

import org.day2.electionmanagmentsystem.vote.VoteAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteActionRepository extends JpaRepository<VoteAction,Long> {
}
