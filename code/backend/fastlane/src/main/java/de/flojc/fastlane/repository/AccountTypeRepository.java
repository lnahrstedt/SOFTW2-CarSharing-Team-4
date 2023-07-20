package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// The code snippet is defining a repository interface called `AccountTypeRepository`. This interface extends the
// `JpaRepository` interface, which is a part of the Spring Data JPA framework.
@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    Optional<AccountType> getAccountTypeByNameIgnoreCase(String accountTypeName);
}