package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// The code snippet is defining an interface called `EmployeeRepository` that extends the `JpaRepository` interface.
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Optional<Employee> findByUserId(Long userId);
}
