package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.Configuration;
import de.flojc.fastlane.model.Equipment;
import de.flojc.fastlane.model.Vehicle;
import de.flojc.fastlane.repository.VehicleRepository;
import de.flojc.fastlane.request.VehicleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The VehicleService class is responsible for handling CRUD operations and business logic related to vehicles.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    private final VehicleModelService vehicleModelService;
    private final ConfigurationService configurationService;
    private final VehicleCategoryService vehicleCategoryService;
    private final VehicleTypeService vehicleTypeService;
    private final VehicleTransmissionService vehicleTransmissionService;
    private final VehicleFuelService vehicleFuelService;
    private final EquipmentService equipmentService;


    /**
     * The function retrieves all vehicles from the vehicle repository and logs the number of vehicles found.
     *
     * @return The method is returning a List of Vehicle objects.
     */
    public List<Vehicle> findAll() {
        log.debug("Starting retrieval of all vehicles...");
        List<Vehicle> vehicles = vehicleRepository.findAll();
        log.debug("Finished retrieval of all vehicles - {} vehicles found", vehicles.size());
        return vehicles;
    }

    /**
     * The function retrieves a vehicle from the vehicle repository based on its ID and throws a BackendException if the
     * vehicle is not found.
     *
     * @param id The "id" parameter is of type Long and represents the unique identifier of the vehicle that needs to be
     * retrieved.
     * @return The method is returning a Vehicle object.
     */
    public Vehicle findById(Long id) throws BackendException {
        log.debug("Starting retrieval of vehicle with id: {}...", id);
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_NOT_FOUND, id));
        log.debug("Finished retrieval of vehicle with id: {}", id);
        return vehicle;
    }

    /**
     * The function saves a vehicle to the repository after converting and validating the vehicle request.
     *
     * @param vehicleRequest The vehicleRequest parameter is an object of type VehicleRequest. It contains the information
     * needed to create or update a vehicle object.
     * @return The method is returning the saved vehicle object.
     */
    public Vehicle save(VehicleRequest vehicleRequest) throws BackendException {
        log.debug("Starting to save a vehicle to the repository...");
        Vehicle vehicle = convertAndValidate(vehicleRequest);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.debug("Finished saving a vehicle to the repository. Id: {}", vehicle.getId());
        return savedVehicle;
    }

    /**
     * The function updates a vehicle's fields based on the provided vehicle request and returns the updated vehicle.
     *
     * @param id The id parameter is of type Long and represents the unique identifier of the vehicle that needs to be
     * updated.
     * @param vehicleRequest The `vehicleRequest` parameter is an object of type `VehicleRequest`. It contains the updated
     * fields for the vehicle that needs to be updated.
     * @return The method is returning a Vehicle object.
     */
    public Vehicle update(Long id, VehicleRequest vehicleRequest) throws BackendException {
        log.debug("Starting update of vehicle with id: {}...", id);
        vehicleRequest.validateFieldsNotBlank();

        Vehicle vehicleToBeUpdated = vehicleRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_NOT_FOUND, id));
        Vehicle vehicleWithUpdatedFields = convertVehicleRequestToVehicle(vehicleRequest);

        if (vehicleWithUpdatedFields.getNumberPlate() != null && !vehicleRepository.existsByNumberPlate(vehicleWithUpdatedFields.getNumberPlate())) {
            vehicleToBeUpdated.setNumberPlate(vehicleWithUpdatedFields.getNumberPlate());
        }

        if (vehicleWithUpdatedFields.getConfiguration() != null) {
            Configuration configuration = updateConfiguration(vehicleToBeUpdated.getConfiguration(), vehicleWithUpdatedFields.getConfiguration());
            vehicleToBeUpdated.setConfiguration(configuration);
        }

        if (vehicleWithUpdatedFields.getMileage() != null) {
            vehicleToBeUpdated.setMileage(vehicleWithUpdatedFields.getMileage());
        }

        if (vehicleWithUpdatedFields.getLatitude() != null) {
            vehicleToBeUpdated.setLatitude(vehicleWithUpdatedFields.getLatitude());
        }

        if (vehicleWithUpdatedFields.getLongitude() != null) {
            vehicleToBeUpdated.setLongitude(vehicleWithUpdatedFields.getLongitude());
        }

        Vehicle vehicle = vehicleRepository.save(vehicleToBeUpdated);
        log.debug("Finished updating of vehicle with id: {}", id);
        return vehicle;
    }

    /**
     * The function deletes a vehicle with the specified id and throws a BackendException if the vehicle is not found.
     *
     * @param id The "id" parameter is a Long value representing the unique identifier of the vehicle that needs to be
     * deleted.
     */
    public void delete(Long id) throws BackendException {
        try {
            log.debug("Starting deletion of vehicle with id: {}...", id);
            HelperService.delete(id, vehicleRepository);
            log.debug("Finished deletion of vehicle with id: {}", id);
        } catch (BackendException e) {
            ErrorCode errorCode = ErrorCode.VEHICLE_NOT_FOUND;
            e.setErrorCode(errorCode);
            e.setDescription(String.format(errorCode.getDescription(), id));
            throw e;
        }
    }

    /**
     * The function finds or saves a vehicle object and returns the result.
     *
     * @param vehicle The "vehicle" parameter is an object of the Vehicle class.
     * @return The method is returning a Vehicle object.
     */
    public Vehicle findOrSave(Vehicle vehicle) {
        log.debug("Starting findOrSave operation for vehicle: {}...", vehicle);
        Vehicle result = HelperService.findOrSave(vehicle, vehicleRepository);
        log.debug("Completed findOrSave operation for vehicle: {}", vehicle);
        return result;
    }


    /**
     * The function updates a vehicle configuration by updating its fields with the values from another configuration
     * object and returns the updated configuration.
     *
     * @param configurationToBeUpdated The configuration object that needs to be updated with the new values.
     * @param configurationWithUpdatedFields The `configurationWithUpdatedFields` parameter is an instance of the
     * `Configuration` class that contains the updated values for the configuration fields.
     * @return The method is returning an updated Configuration object.
     */
    private Configuration updateConfiguration(Configuration configurationToBeUpdated, Configuration configurationWithUpdatedFields) throws BackendException {
        log.debug("Updating configuration of vehicle...");

        if (configurationWithUpdatedFields.getVehicleCategory() != null) {
            Character vehicleCategoryId = configurationWithUpdatedFields.getVehicleCategory().getId();
            configurationToBeUpdated.setVehicleCategory(vehicleCategoryService.findById(vehicleCategoryId));
        }

        if (configurationWithUpdatedFields.getVehicleType() != null) {
            Character vehicleTypeId = configurationWithUpdatedFields.getVehicleType().getId();
            configurationToBeUpdated.setVehicleType(vehicleTypeService.findById(vehicleTypeId));
        }

        if (configurationWithUpdatedFields.getVehicleTransmission() != null) {
            Character vehicleTransmissionId = configurationWithUpdatedFields.getVehicleTransmission().getId();
            configurationToBeUpdated.setVehicleTransmission(vehicleTransmissionService.findById(vehicleTransmissionId));
        }

        if (configurationWithUpdatedFields.getVehicleFuel() != null) {
            Character vehicleFuelId = configurationWithUpdatedFields.getVehicleFuel().getId();
            configurationToBeUpdated.setVehicleFuel(vehicleFuelService.findById(vehicleFuelId));
        }

        if (configurationWithUpdatedFields.getEquipment() != null) {
            Equipment updatedEquipment = updateEquipment(configurationToBeUpdated.getEquipment(), configurationWithUpdatedFields.getEquipment());
            configurationToBeUpdated.setEquipment(updatedEquipment);
        }

        Configuration updatedConfiguration = configurationService.findOrSave(configurationToBeUpdated);
        log.debug("Updated configuration successfully");
        return updatedConfiguration;
    }

    /**
     * The function updates the fields of an Equipment object and returns the updated Equipment object.
     *
     * @param equipmentToBeUpdated The equipment object that needs to be updated with the new values.
     * @param equipmentWithUpdatedFields This parameter is an instance of the Equipment class that contains the updated
     * values for the equipment fields.
     * @return The method is returning an updated Equipment object.
     */
    private Equipment updateEquipment(Equipment equipmentToBeUpdated, Equipment equipmentWithUpdatedFields) {
        log.debug("Updating equipment of vehicle...");

        if (equipmentWithUpdatedFields.hasAc() != null) {
            equipmentToBeUpdated.hasAc(equipmentWithUpdatedFields.hasAc());
        }

        if (equipmentWithUpdatedFields.hasNavigation() != null) {
            equipmentToBeUpdated.hasNavigation(equipmentWithUpdatedFields.hasNavigation());
        }

        if (equipmentWithUpdatedFields.hasCruiseControl() != null) {
            equipmentToBeUpdated.hasCruiseControl(equipmentWithUpdatedFields.hasCruiseControl());
        }

        if (equipmentWithUpdatedFields.hasDrivingAssistant() != null) {
            equipmentToBeUpdated.hasDrivingAssistant(equipmentWithUpdatedFields.hasDrivingAssistant());
        }

        Equipment updatedEquipment = equipmentService.findOrSave(equipmentToBeUpdated);
        log.debug("Updated equipment successfully");
        return updatedEquipment;
    }

    /**
     * The function converts and validates a VehicleRequest object, ensuring that all fields are not null or blank, and
     * then converts it to a Vehicle object, validates the number plate is not used twice, and saves the equipment and
     * configuration before returning the vehicle.
     *
     * @param vehicleRequest The vehicleRequest parameter is an object of type VehicleRequest. It contains the necessary
     * information to create a new Vehicle object.
     * @return The method is returning a Vehicle object.
     */
    private Vehicle convertAndValidate(VehicleRequest vehicleRequest) throws BackendException {
        vehicleRequest.validateFieldsNotNullOrBlank();
        Vehicle vehicle = convertVehicleRequestToVehicle(vehicleRequest);
        validateNumberplateNotUsedTwice(vehicle);
        vehicle.getConfiguration().setEquipment(equipmentService.findOrSave(vehicle.getConfiguration().getEquipment()));
        vehicle.setConfiguration(configurationService.findOrSave(vehicle.getConfiguration()));
        return vehicle;
    }

    /**
     * The function validates that a vehicle's number plate is not used twice.
     *
     * @param vehicle The vehicle object that contains the number plate to be validated.
     */
    private void validateNumberplateNotUsedTwice(Vehicle vehicle) throws BackendException {
        log.debug("Starting to validate number plate of vehicle is not used twice...");
        if (vehicleRepository.existsByNumberPlate(vehicle.getNumberPlate())) {
            throw new BackendException(ErrorCode.VEHICLE_NUMBER_PLATE_ALREADY_EXIST, vehicle.getNumberPlate());
        }
        log.debug("Finished validating the vehicle's number plate.");
    }

    /**
     * The function converts a list of VehicleRequest objects to a list of Vehicle objects, validates them, and saves them
     * to the vehicle repository.
     *
     * @param vehicleRequests A list of VehicleRequest objects.
     */
    public void convertListOfVehicleRequestToListOfVehicleModelAndSave(List<VehicleRequest> vehicleRequests) throws BackendException {
        log.debug("Converting list of VehicleRequest to list of Vehicle...");
        List<Vehicle> vehicles = new ArrayList<>();
        for (VehicleRequest vehicleRequest : vehicleRequests) {
            vehicles.add(convertAndValidate(vehicleRequest));
        }
        vehicleRepository.saveAll(vehicles);
        log.debug("Finished converting list of VehicleModelRequest to list of Driver");
    }

    /**
     * The function converts a VehicleRequest object into a Vehicle object by mapping the properties of the VehicleRequest
     * to the corresponding properties of the Vehicle.
     *
     * @param vehicleRequest The `vehicleRequest` parameter is an object of type `VehicleRequest`. It contains information
     * about a vehicle that needs to be converted into a `Vehicle` object.
     * @return The method is returning a Vehicle object.
     */
    public Vehicle convertVehicleRequestToVehicle(VehicleRequest vehicleRequest) throws BackendException {

        Equipment equipment = Equipment.builder()
                .hasAc(vehicleRequest.getHasAc())
                .hasCruiseControl(vehicleRequest.getHasCruiseControl())
                .hasDrivingAssistant(vehicleRequest.getHasDrivingAssistant())
                .hasNavigation(vehicleRequest.getHasNavigation())
                .build();

        Configuration configuration = Configuration.builder()
                .vehicleCategory(vehicleRequest.getVehicleCategory() == null ? null : vehicleCategoryService.findById(vehicleRequest.getVehicleCategory()))
                .vehicleType(vehicleRequest.getVehicleType() == null ? null : vehicleTypeService.findById(vehicleRequest.getVehicleType()))
                .vehicleTransmission(vehicleRequest.getVehicleTransmission() == null ? null : vehicleTransmissionService.findById(vehicleRequest.getVehicleTransmission()))
                .vehicleFuel(vehicleRequest.getVehicleFuel() == null ? null : vehicleFuelService.findById(vehicleRequest.getVehicleFuel()))
                .equipment(equipment)
                .build();


        return Vehicle.builder()
                .vehicleModel(vehicleRequest.getVehicleModel() == null ? null : vehicleModelService.findByName(vehicleRequest.getVehicleModel()))
                .constructionYear(vehicleRequest.getConstructionYear())
                .mileage(vehicleRequest.getMileage())
                .latitude(vehicleRequest.getLatitude())
                .longitude(vehicleRequest.getLongitude())
                .numberPlate(vehicleRequest.getNumberPlate())
                .configuration(configuration)
                .build();
    }
}