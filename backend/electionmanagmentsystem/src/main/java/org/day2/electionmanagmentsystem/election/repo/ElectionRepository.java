package org.day2.electionmanagmentsystem.election.repo;

import org.day2.electionmanagmentsystem.election.Election;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface ElectionRepository extends JpaRepository<Election,Long> {
    Optional<Election> findByPublicId(UUID publicId);
    Optional<Election> findByPublicIdAndUserPublicId(UUID electionPublicId, UUID userPublicId);
}
