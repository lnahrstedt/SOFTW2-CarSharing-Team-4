package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.VehicleBrand;
import de.flojc.fastlane.repository.VehicleBrandlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The VehicleBrandService class is a service class that provides methods for retrieving, saving, and finding vehicle
 * brands.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleBrandService {

    private final VehicleBrandlRepository vehicleBrandlRepository;

    /**
     * The function retrieves all vehicle brands from the repository and logs the number of brands found.
     *
     * @return The method is returning a List of VehicleBrand objects.
     */
    public List<VehicleBrand> findAll() {
        log.debug("Starting retrieval of all vehicle brands...");
        List<VehicleBrand> vehicleBrands = vehicleBrandlRepository.findAll();
        log.debug("Finished retrieval of all vehicle brands - {} vehicle brands found.", vehicleBrands.size());
        return vehicleBrands;
    }

    /**
     * The function retrieves a vehicle brand by its ID and throws a BackendException if the brand is not found.
     *
     * @param id The "id" parameter is a long value representing the unique identifier of the vehicle brand that needs to
     * be retrieved.
     * @return The method is returning a VehicleBrand object.
     */
    public VehicleBrand findById(long id) throws BackendException {
        log.debug("Starting retrieval of vehicle brand with id: {}...", id);
        VehicleBrand vehicleBrand = vehicleBrandlRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_BRAND_NOT_FOUND, id));
        log.debug("Finished retrieval of vehicle brand with id: {}", id);
        return vehicleBrand;
    }

    /**
     * The function finds a vehicle brand by its name and returns it, or throws an exception if the brand is not found.
     *
     * @param name The "name" parameter is a String that represents the name of the vehicle brand that we want to find.
     * @return The method is returning a VehicleBrand object.
     */
    public VehicleBrand findByName(String name) throws BackendException {
        log.debug("Starting retrieval of vehicle brand with name: {}...", name);
        VehicleBrand vehicleBrand = vehicleBrandlRepository.findByBrandName(name).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_BRAND_NOT_FOUND, name));
        log.debug("Finished retrieval of vehicle brand with name: {}", name);
        return vehicleBrand;
    }

    /**
     * The function saves a vehicle brand to the repository and returns the saved vehicle brand.
     *
     * @param vehicleBrand The parameter "vehicleBrand" is an object of type VehicleBrand.
     * @return The method is returning a VehicleBrand object.
     */
    public VehicleBrand save(VehicleBrand vehicleBrand) throws BackendException {
        log.debug("Starting to save an vehicle brand to the repository...");
        VehicleBrand savedVehicleBrand = findOrSave(vehicleBrand);
        log.debug("Finished saving an vehicle brand to the repository. Id of saved vehicle brand: {}", vehicleBrand.getId());
        return savedVehicleBrand;
    }

    /**
     * The function finds or saves a vehicle brand and returns the result.
     *
     * @param vehicleBrand The vehicle brand object that needs to be found or saved.
     * @return The method is returning a VehicleBrand object.
     */
    public VehicleBrand findOrSave(VehicleBrand vehicleBrand) {
        log.debug("Starting findOrSave operation for vehicle brand: {}...", vehicleBrand);
        VehicleBrand result = HelperService.findOrSave(vehicleBrand, vehicleBrandlRepository);
        log.debug("Completed findOrSave operation for vehicle brand: {}", vehicleBrand);
        return result;
    }
}
