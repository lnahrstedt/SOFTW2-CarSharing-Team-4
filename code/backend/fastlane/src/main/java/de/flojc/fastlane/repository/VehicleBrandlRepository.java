package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.VehicleBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// The code snippet is defining a repository interface called `VehicleBrandlRepository`. This interface extends the
// `JpaRepository` interface, which is a part of the Spring Data JPA framework.
public interface VehicleBrandlRepository extends JpaRepository<VehicleBrand, Long> {
    Optional<VehicleBrand> findByBrandName(String brandName);

}
