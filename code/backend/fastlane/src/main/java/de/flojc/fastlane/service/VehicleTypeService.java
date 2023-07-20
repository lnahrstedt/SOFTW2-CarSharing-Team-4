package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.VehicleType;
import de.flojc.fastlane.repository.VehicleTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The VehicleTypeService class is a Java service that retrieves a vehicle type by its ID from a repository and throws a
 * BackendException if the vehicle type is not found.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleTypeService {

    private final VehicleTypeRepository vehicleTypeRepository;

    /**
     * This function retrieves a vehicle type by its ID and throws a BackendException if the vehicle type is not found.
     *
     * @param vehicleTypeId The vehicleTypeId parameter is of type Character and represents the unique identifier of the
     * vehicle type that needs to be retrieved.
     * @return The method is returning an object of type VehicleType.
     */
    public VehicleType findById(Character vehicleTypeId) throws BackendException {
        log.debug("Starting retrieval of vehicle type with id: {}...", vehicleTypeId);
        VehicleType result = vehicleTypeRepository.findById(vehicleTypeId).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_TYPE_NOT_FOUND, vehicleTypeId));
        log.debug("Finished retrieval of vehicle type with id: {}", vehicleTypeId);
        return result;
    }
}
