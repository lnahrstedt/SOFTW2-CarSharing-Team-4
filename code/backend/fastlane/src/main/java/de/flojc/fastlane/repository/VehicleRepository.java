package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface called `VehicleRepository` that extends the `JpaRepository`
// interface.
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByNumberPlate(String numberPlate);
}
