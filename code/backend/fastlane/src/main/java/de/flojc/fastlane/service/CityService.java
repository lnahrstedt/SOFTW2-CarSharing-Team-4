package de.flojc.fastlane.service;

import de.flojc.fastlane.model.City;
import de.flojc.fastlane.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The CityService class is a service that finds or saves a City object using a CityRepository and logs the operation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    /**
     * The function finds or saves a city object and returns the result.
     *
     * @param city The "city" parameter is an object of type City, which represents a city entity.
     * @return The method is returning an object of type City.
     */
    public City findOrSave(City city) {
        log.debug("Starting findOrSave operation for city: {}...", city);
        City result = HelperService.findOrSave(city, cityRepository);
        log.debug("Completed findOrSave operation for city: {}. Result: {}", city, result);
        return result;
    }
}