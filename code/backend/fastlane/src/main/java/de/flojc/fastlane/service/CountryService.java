package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.Country;
import de.flojc.fastlane.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The CountryService class is a Java service that retrieves a country from a repository based on its country code.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    /**
     * The function findByCountryCode searches for a country in the country repository based on the given country code and
     * returns the result.
     *
     * @param countryCode The parameter "countryCode" is a String that represents the code of a country.
     * @return The method is returning an object of type Country.
     */
    public Country findByCountryCode(String countryCode) throws BackendException {
        log.debug("Starting findByCountryCode operation for country code: {}...", countryCode);
        Country result = countryRepository.findById(countryCode).orElseThrow(() ->
                new BackendException(ErrorCode.COUNTRY_NOT_FOUND, countryCode));
        log.debug("Completed findByCountryCode operation for country code: {}. Result: {}", countryCode, result);
        return result;
    }
}