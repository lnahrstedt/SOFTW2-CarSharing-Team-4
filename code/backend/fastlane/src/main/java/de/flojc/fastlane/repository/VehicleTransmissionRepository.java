package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.VehicleTransmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface called `VehicleTransmissionRepository` for accessing and manipulating data
// related to the `VehicleTransmission` entity.
@Repository
public interface VehicleTransmissionRepository extends JpaRepository<VehicleTransmission, Character> {
}
