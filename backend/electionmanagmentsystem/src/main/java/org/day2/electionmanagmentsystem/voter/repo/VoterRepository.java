package org.day2.electionmanagmentsystem.voter.repo;

import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.voter.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoterRepository extends JpaRepository<Voter,Long> {
    Optional <Voter> findByPublicId(String publicId);
    Optional <Voter> findByElectionAndUser(Election election, User user);

    List <Voter> findByElection(Election election);
    boolean existsByElection(Election election);
    List <Voter> findByUser(User user);
}
