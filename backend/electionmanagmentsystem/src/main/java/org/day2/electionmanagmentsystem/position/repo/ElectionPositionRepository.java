package org.day2.electionmanagmentsystem.position.repo;

import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ElectionPositionRepository extends JpaRepository<ElectionPosition, Long> {

    List<ElectionPosition> findByElectionOrderByNameAsc(Election election);

    Optional<ElectionPosition> findByPublicIdAndElectionPublicId(UUID positionPublicId, UUID publicId);

    @Query("""
            SELECT LOWER(ep.name)
            FROM ElectionPosition ep
            WHERE ep.election.id = :electionId
              AND LOWER(ep.name) IN :requestedNames
            """)
    List<String> findExistingPositionNames(
            @Param("electionId") Long electionId,
            @Param("requestedNames") Set<String> requestedNames
    );
    List<ElectionPosition> findByElection(Election election);
}