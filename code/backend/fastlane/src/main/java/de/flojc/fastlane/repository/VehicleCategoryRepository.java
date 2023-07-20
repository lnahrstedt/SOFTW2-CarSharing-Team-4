package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface called `VehicleCategoryRepository` for accessing and manipulating data
// related to the `VehicleCategory` entity.
@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, Character> {
}
