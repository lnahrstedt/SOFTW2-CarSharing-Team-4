package de.flojc.fastlane.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * The HealthService class is a Java service that provides methods for handling ping requests and checking databases.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HealthService {

    private final ApplicationContext applicationContext;

    /**
     * The function "ping" logs a debug message, retrieves the current time, logs another debug message, and returns the
     * current time.
     *
     * @return The method is returning a LocalDateTime object, which represents the current date and time.
     */
    public LocalDateTime ping() {
        log.debug("Received ping request...");
        LocalDateTime currentTime = LocalDateTime.now();
        log.debug("Responded to ping request with current time: {}", currentTime);
        return currentTime;
    }

    /**
     * The function "checkDatabases" logs the start and completion of a database check operation and calls the
     * "findAllInAllRepositories" method.
     */
    public void checkDatabases() {
        log.debug("Starting database check operation...");
        findAllInAllRepositories();
        log.debug("Completed database check operation");
    }

    /**
     * The function performs a findAll operation in all repositories and logs the start and completion of each operation.
     */
    public void findAllInAllRepositories() {
        log.debug("Starting findAll operation in all repositories...");
        Map<String, CrudRepository> repositories = applicationContext.getBeansOfType(CrudRepository.class);
        for (CrudRepository repository : repositories.values()) {
            log.debug("Starting findAll operation for repository: {}", repository.getClass().getName());
            repository.findAll();
            log.debug("Completed findAll operation for repository: {}", repository.getClass().getName());
        }
        log.debug("Completed findAll operation in all repositories");
    }
}