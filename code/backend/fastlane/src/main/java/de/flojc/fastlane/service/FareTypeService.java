package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.FareType;
import de.flojc.fastlane.repository.FareTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The FareTypeService class is a service that retrieves a FareType object by its name from a FareTypeRepository and throws
 * a BackendException if the FareType is not found.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FareTypeService {

    private final FareTypeRepository fareTypeRepository;

    /**
     * The function `getFareTypeByName` retrieves a `FareType` object by its name, logging the start and completion of the
     * operation and throwing a `BackendException` if the fare type is not found.
     *
     * @param fareTypeName The parameter "fareTypeName" is a String that represents the name of a fare type.
     * @return The method is returning a FareType object.
     */
    public FareType getFareTypeByName(String fareTypeName) throws BackendException {
        log.debug("Starting getFareTypeByName operation for fare type name: {}...", fareTypeName);
        FareType result = fareTypeRepository.getFareTypeByNameIgnoreCase(fareTypeName).orElseThrow(() ->
                new BackendException(ErrorCode.FARE_TYPE_NOT_FOUND, fareTypeName));
        log.debug("Completed getFareTypeByName operation for fare type name: {}. Result: {}", fareTypeName, result);
        return result;
    }
}