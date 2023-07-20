package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.FareType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// The code snippet is defining a repository interface called `FareTypeRepository`. This interface extends the
// `JpaRepository` interface, which is a part of the Spring Data JPA framework.
@Repository
public interface FareTypeRepository extends JpaRepository<FareType, Long> {
    Optional<FareType> getFareTypeByNameIgnoreCase(String name);

}
