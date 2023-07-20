package de.flojc.fastlane.controller;

import de.flojc.fastlane.service.HealthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * The HealthController class is a REST controller that handles requests related to health checks, including pinging the
 * server and checking the database health.
 */
@Slf4j
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    /**
     * The function handles a GET request to "/ping" and returns the current LocalDateTime as a ResponseEntity.
     *
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        log.info("Received ping request");
        LocalDateTime ping = healthService.ping();
        log.info("Ping responded at {}", ping);
        return ResponseEntity.ok(ping);
    }

    /**
     * The function checks the health of the database and returns an appropriate response.
     *
     * @return The method is returning a ResponseEntity object. If the database health check is successful, it returns a
     * ResponseEntity with HTTP status code 200 (OK) and an empty body. If the database health check fails, it returns a
     * ResponseEntity with HTTP status code 500 (Internal Server Error) and an empty body.
     */
    @GetMapping("/database")
    public ResponseEntity<?> checkDataBaseHealth() {
        log.info("Received request to check database health");
        try {
            healthService.checkDatabases();
            log.info("Database health check passed");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Database health check failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}