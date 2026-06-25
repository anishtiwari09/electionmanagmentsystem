package org.day2.electionmanagmentsystem.position.repo;

import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface ElectionPositionRepository extends JpaRepository<ElectionPosition,Long> {
     List<ElectionPosition> findByElectionOrderByNameAsc(Election election);
}
