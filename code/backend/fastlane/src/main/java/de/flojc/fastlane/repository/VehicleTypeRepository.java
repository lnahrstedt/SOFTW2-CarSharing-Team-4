package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface called `VehicleTypeRepository` for accessing and manipulating data
// related to the `VehicleType` entity.
@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Character> {
}
