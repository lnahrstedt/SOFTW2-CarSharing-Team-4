package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface called `ConfigurationRepository`.
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
}
