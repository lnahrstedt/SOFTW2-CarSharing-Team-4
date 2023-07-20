package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.VehicleTransmission;
import de.flojc.fastlane.repository.VehicleTransmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The VehicleTransmissionService class is a service that retrieves a vehicle transmission by its ID from a repository and
 * throws a BackendException if it is not found.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleTransmissionService {

    private final VehicleTransmissionRepository vehicleTransmissionRepository;

    /**
     * This function retrieves a vehicle transmission by its ID and throws an exception if it is not found.
     *
     * @param vehicleTransmissionId The vehicleTransmissionId is a unique identifier for a specific vehicle transmission.
     * It is of type Character, which means it represents a single character value.
     * @return The method is returning a VehicleTransmission object.
     */
    public VehicleTransmission findById(Character vehicleTransmissionId) throws BackendException {
        log.debug("Starting retrieval of vehicle transmission with id: {}...", vehicleTransmissionId);
        VehicleTransmission result = vehicleTransmissionRepository.findById(vehicleTransmissionId).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_TRANSMISSION_NOT_FOUND, vehicleTransmissionId));
        log.debug("Finished retrieval of vehicle transmission with id: {}", vehicleTransmissionId);
        return result;
    }
}
