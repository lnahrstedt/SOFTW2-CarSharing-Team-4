package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// The code snippet is defining a repository interface called `VehicleModelRepository`. This interface extends the
// `JpaRepository` interface, which is a part of the Spring Data JPA framework.
public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long> {
    Optional<VehicleModel> findByModelName(String modelName);

}
