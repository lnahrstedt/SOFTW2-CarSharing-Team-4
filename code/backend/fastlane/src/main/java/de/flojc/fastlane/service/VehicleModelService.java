package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.VehicleBrand;
import de.flojc.fastlane.model.VehicleModel;
import de.flojc.fastlane.repository.VehicleModelRepository;
import de.flojc.fastlane.request.VehicleModelRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The VehicleModelService class is a service class that provides methods for retrieving, saving, and converting vehicle
 * models.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;

    private final VehicleBrandService vehicleBrandService;

    /**
     * The function retrieves all vehicle models from the repository and logs the number of models found.
     *
     * @return The method is returning a List of VehicleModel objects.
     */
    public List<VehicleModel> findAll() {
        log.debug("Starting retrieval of all vehicle models...");
        List<VehicleModel> vehicleModels = vehicleModelRepository.findAll();
        log.debug("Finished retrieval of all vehicle models - {} vehicle models found.", vehicleModels.size());
        return vehicleModels;
    }

    /**
     * The function retrieves a vehicle model by its ID and throws a BackendException if the model is not found.
     *
     * @param id The "id" parameter is a long value representing the unique identifier of the vehicle model that needs to
     * be retrieved.
     * @return The method is returning a VehicleModel object.
     */
    public VehicleModel findById(long id) throws BackendException {
        log.debug("Starting retrieval of vehicle model with id: {}...", id);
        VehicleModel vehicleModel = vehicleModelRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_MODEL_NOT_FOUND, id));
        log.debug("Finished retrieval of vehicle model with id: {}", id);
        return vehicleModel;
    }

    /**
     * The function finds a vehicle model by its name and returns it, or throws an exception if the model is not found.
     *
     * @param name The parameter "name" is a String that represents the name of the vehicle model that we want to find.
     * @return The method is returning a VehicleModel object.
     */
    public VehicleModel findByName(String name) throws BackendException {
        log.debug("Starting retrieval of vehicle model with email: {}...", name);
        VehicleModel vehicleModel = vehicleModelRepository.findByModelName(name).orElseThrow(() -> new BackendException(ErrorCode.VEHICLE_MODEL_NOT_FOUND, name));
        log.debug("Finished retrieval of vehicle model with email: {}", name);
        return vehicleModel;
    }

    /**
     * The function saves a vehicle model to a repository and returns the saved vehicle model.
     *
     * @param vehicleModel The parameter "vehicleModel" is an object of type VehicleModel, which represents a model of a
     * vehicle.
     * @return The method is returning a VehicleModel object.
     */
    public VehicleModel save(VehicleModel vehicleModel) throws BackendException {
        log.debug("Starting to save an vehicle model to the repository...");
        VehicleModel savedVehicleModel = findOrSave(vehicleModel);
        log.debug("Finished saving an vehicle model to the repository. Id of saved vehicle model: {}", vehicleModel.getId());
        return savedVehicleModel;
    }

    /**
     * The function finds or saves a vehicle model and returns the result.
     *
     * @param vehicleModel The parameter "vehicleModel" is an instance of the VehicleModel class. It represents the vehicle
     * model that needs to be found or saved in the database.
     * @return The method is returning a VehicleModel object.
     */
    public VehicleModel findOrSave(VehicleModel vehicleModel) {
        log.debug("Starting findOrSave operation for vehicle model: {}...", vehicleModel);
        VehicleModel result = HelperService.findOrSave(vehicleModel, vehicleModelRepository);
        log.debug("Completed findOrSave operation for vehicle model: {}", vehicleModel);
        return result;
    }

    /**
     * The function converts a list of VehicleModelRequest objects to a list of VehicleModel objects and saves them.
     *
     * @param vehicleModelRequests A list of VehicleModelRequest objects.
     */
    public void convertListOfVehicleModelRequestToListOfVehicleModelAndSave(List<VehicleModelRequest> vehicleModelRequests) throws BackendException {
        log.debug("Converting list of VehicleModelRequest to list of Account...");
        for (VehicleModelRequest vehicleModelRequest : vehicleModelRequests) {
            convertVehicleModelRequestToVehicleModelAndSave(vehicleModelRequest);
        }
        log.debug("Finished converting list of VehicleModelRequest to list of Driver");
    }

    /**
     * The function converts a VehicleModelRequest object into a VehicleModel object, saves it, and associates it with a
     * VehicleBrand object.
     *
     * @param vehicleModelRequest The vehicleModelRequest parameter is an object of type VehicleModelRequest. It contains
     * information about a vehicle model, such as the model name, brand name, and a link to a picture.
     */
    private void convertVehicleModelRequestToVehicleModelAndSave(VehicleModelRequest vehicleModelRequest) throws BackendException {
        vehicleModelRequest.validateFieldsNotNullOrBlank();

        VehicleBrand vehicleBrand = vehicleBrandService.findByName(vehicleModelRequest.getBrandName());

        VehicleModel vehicleModel = VehicleModel.builder()
                .modelName(vehicleModelRequest.getModelName())
                .vehicleBrand(vehicleBrand)
                .linkToPicture(vehicleModelRequest.getLinkToPicture())
                .build();

        findOrSave(vehicleModel);
    }
}
