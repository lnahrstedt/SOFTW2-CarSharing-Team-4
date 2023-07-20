package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface for the `Address` entity class.
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}