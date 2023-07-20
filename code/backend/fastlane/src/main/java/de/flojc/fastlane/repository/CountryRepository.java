package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface called `CountryRepository` for accessing and manipulating data
// related to the `Country` entity.
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
}
