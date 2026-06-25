package org.day2.electionmanagmentsystem.election;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ElectionRepository extends JpaRepository<Election,Long> {
    Optional<Election> findByPublicId(UUID publicId);
}
