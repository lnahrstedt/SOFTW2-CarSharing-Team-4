package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// The code snippet is defining a repository interface called `CityRepository` that extends the `JpaRepository` interface.
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findCityByName(String name);
}
