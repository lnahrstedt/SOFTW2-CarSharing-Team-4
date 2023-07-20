package de.flojc.fastlane.service;

import de.flojc.fastlane.model.Location;
import de.flojc.fastlane.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The LocationService class is a Java service that finds or saves a location using a LocationRepository and logs the
 * operation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    /**
     * The function finds or saves a location and returns the result.
     *
     * @param location The "location" parameter is an object of the Location class.
     * @return The method is returning a Location object.
     */
    public Location findOrSave(Location location) {
        log.debug("Starting findOrSave operation for location: {}...", location);
        Location result = HelperService.findOrSave(location, locationRepository);
        log.debug("Completed findOrSave operation for location: {}. Result: {}", location, result);
        return result;
    }
}