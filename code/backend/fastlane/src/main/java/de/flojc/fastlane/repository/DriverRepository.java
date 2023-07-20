package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// The code snippet is defining a repository interface called `DriverRepository` for accessing and manipulating data
// related to the `Driver` entity.
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByLicenseId(String licenseId);

    Optional<Driver> findByUserId(Long userId);

    boolean existsByLicenseId(String licenseId);

}