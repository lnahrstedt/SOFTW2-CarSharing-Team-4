package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// The code snippet is defining a repository interface called `AccountRepository`. This interface extends the
// `JpaRepository` interface, which is a part of the Spring Data JPA framework.
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    List<Account> findAccountsByUserId(Long userId);

    boolean existsByEmail(String email);
}