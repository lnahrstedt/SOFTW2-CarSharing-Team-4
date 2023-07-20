package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The `@Repository` annotation is used to indicate that the `EquipmentRepository` interface is a repository component in
// the Spring framework.
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
