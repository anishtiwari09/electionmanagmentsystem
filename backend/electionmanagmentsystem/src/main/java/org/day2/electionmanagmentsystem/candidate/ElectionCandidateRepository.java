package org.day2.electionmanagmentsystem.candidate;

import org.day2.electionmanagmentsystem.Position.ElectionPosition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ElectionCandidateRepository extends JpaRepository<ElectionCandidate,Long> {
    List <ElectionCandidate> findByPositionOrderByCreatedAtAsc(ElectionPosition position);
    List<ElectionCandidate> findByPositionInOrderByCreatedAtAsc(
            List<ElectionPosition> positions
    );
    List <ElectionCandidate> findByPublicIdIn(List <UUID> publicIds);
}