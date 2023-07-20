package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface called `LocationRepository` for accessing and manipulating data
// related to the `Location` entity.
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

}
