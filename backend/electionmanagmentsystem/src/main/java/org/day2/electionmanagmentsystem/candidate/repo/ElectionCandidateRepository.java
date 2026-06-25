package org.day2.electionmanagmentsystem.candidate;

import lombok.Getter;
import org.day2.electionmanagmentsystem.position.ElectionPosition;

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
}