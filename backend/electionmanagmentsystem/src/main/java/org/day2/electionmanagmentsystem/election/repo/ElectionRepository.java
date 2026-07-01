package org.day2.electionmanagmentsystem.election.repo;

import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ElectionRepository extends JpaRepository<Election,Long> {
    Optional<Election> findByPublicId(UUID publicId);
    Optional<Election> findByPublicIdAndUserPublicId(UUID electionPublicId, UUID userPublicId);
    Page<Election> findByUserPublicIdOrderByUpdatedAtDesc(UUID userPublicId,Pageable pageable);
    Page<Election> findByUserPublicIdAndStatusInOrderByUpdatedAtDesc(
            UUID userPublicId,
            List<ElectionStatus> status,
            Pageable pageable

    );

    UUID user(User user);
}
