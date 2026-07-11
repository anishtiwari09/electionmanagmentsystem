package org.day2.electionmanagmentsystem.user.repo;

import org.day2.electionmanagmentsystem.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPublicId(UUID publicId);

    Optional<User> findByEmailIgnoreCase(String email);
    List<User> findByEmailIn(Set<String> emails);
}
