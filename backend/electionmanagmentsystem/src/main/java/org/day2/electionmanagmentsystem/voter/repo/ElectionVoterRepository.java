package org.day2.electionmanagmentsystem.voter.repo;

import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.voter.ElectionVoter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectionVoterRepository extends JpaRepository<ElectionVoter,Long> {
    Optional <ElectionVoter> findByPublicId(String publicId);
    Optional <ElectionVoter> findByElectionAndUser(Election election, User user);
}
