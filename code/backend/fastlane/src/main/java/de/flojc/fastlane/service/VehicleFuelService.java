package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.VehicleFuel;
import de.flojc.fastlane.repository.VehicleFuelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The VehicleFuelService class is a service that retrieves a VehicleFuel object by its ID from a repository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleFuelService {

    private final VehicleFuelRepository vehicleFuelRepository;

    /**
     * The function retrieves a vehicle fuel object by its ID and throws a BackendException if it is not found.
     *
     * @param vehicleFuelId The vehicleFuelId is a unique identifier for a specific vehicle fuel. It is of type Character,
     * which means it can hold a single character value.
     * @return The method is returning an object of type VehicleFuel.
     */
    public VehicleFuel findById(Character vehicleFuelId) throws BackendException {
        log.debug("Starting retrieval of vehicle fuel with id: {}...", vehicleFuelId);
        VehicleFuel result = vehicleFuelRepository.findById(vehicleFuelId).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_FUEL_NOT_FOUND, vehicleFuelId));
        log.debug("Finished retrieval of vehicle fuel with id: {}", vehicleFuelId);
        return result;
    }
}
