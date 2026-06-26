package org.day2.electionmanagmentsystem.candidate.repo;

import org.day2.electionmanagmentsystem.candidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.position.ElectionPosition;

import org.day2.electionmanagmentsystem.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ElectionCandidateRepository extends JpaRepository<ElectionCandidate,Long> {
    List <ElectionCandidate> findByPositionOrderByCreatedAtAsc(ElectionPosition position);
    List<ElectionCandidate> findByPositionInOrderByCreatedAtAsc(
            List<ElectionPosition> positions
    );
    List <ElectionCandidate> findByPublicIdIn(List <UUID> publicIds);

    boolean existsByPositionAndUser(ElectionPosition position, User user);
}