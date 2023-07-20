package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.VehicleCategory;
import de.flojc.fastlane.repository.VehicleCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The VehicleCategoryService class is a Java service that retrieves a vehicle category by its ID from a repository and
 * throws a BackendException if the category is not found.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleCategoryService {

    private final VehicleCategoryRepository vehicleCategoryRepository;

    /**
     * The function retrieves a vehicle category by its ID and returns it, or throws an exception if the category is not
     * found.
     *
     * @param vehicleCategoryId The vehicleCategoryId is a character that represents the unique identifier of the vehicle
     * category that we want to retrieve.
     * @return The method is returning a VehicleCategory object.
     */
    public VehicleCategory findById(Character vehicleCategoryId) throws BackendException {
        log.debug("Starting retrieval of vehicle category with id: {}...", vehicleCategoryId);
        VehicleCategory result = vehicleCategoryRepository.findById(vehicleCategoryId).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_CATEGORY_NOT_FOUND, vehicleCategoryId));
        log.debug("Finished retrieval of vehicle category with id: {}", vehicleCategoryId);
        return result;
    }
}
