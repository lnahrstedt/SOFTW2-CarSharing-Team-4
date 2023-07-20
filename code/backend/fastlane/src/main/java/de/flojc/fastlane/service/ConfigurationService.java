package de.flojc.fastlane.service;

import de.flojc.fastlane.model.Configuration;
import de.flojc.fastlane.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The ConfigurationService class is a Java service that finds or saves a Configuration object using a
 * ConfigurationRepository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    /**
     * The function finds or saves a configuration object and logs the operation.
     *
     * @param configuration The "configuration" parameter is an object of type Configuration.
     * @return The method is returning a Configuration object.
     */
    public Configuration findOrSave(Configuration configuration) {
        log.debug("Starting findOrSave operation for configuration: {}...", configuration);
        Configuration result = HelperService.findOrSave(configuration, configurationRepository);
        log.debug("Completed findOrSave operation for configuration: {}. Result: {}", configuration, result);
        return result;
    }
}