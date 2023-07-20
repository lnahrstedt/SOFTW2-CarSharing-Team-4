package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.VehicleFuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface called `VehicleFuel` for accessing and manipulating data
// related to the `VehicleFuel` entity.
@Repository
public interface VehicleFuelRepository extends JpaRepository<VehicleFuel, Character> {
}
