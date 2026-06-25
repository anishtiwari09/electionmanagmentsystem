package org.day2.electionmanagmentsystem.Position;

import lombok.Data;
import org.day2.electionmanagmentsystem.election.Election;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface ElectionPositionRepository extends JpaRepository<ElectionPosition,Long> {
     List<ElectionPosition> findByElectionOrderByNameAsc(Election election);
}
