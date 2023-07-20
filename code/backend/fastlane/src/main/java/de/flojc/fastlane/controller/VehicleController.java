package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.model.Vehicle;
import de.flojc.fastlane.request.VehicleRequest;
import de.flojc.fastlane.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


/**
 * The VehicleController class is a REST controller that handles requests related to vehicles, including retrieving all
 * vehicles, retrieving a vehicle by ID, saving a new vehicle, updating a vehicle, and deleting a vehicle.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * The function retrieves all vehicles and returns them as a response entity.
     *
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping
    //Tested
    public ResponseEntity<?> getVehicles() {
        log.info("Received request to get all vehicles");
        var vehicles = vehicleService.findAll();
        log.info("Fetched {} vehicles", vehicles.size());
        return ResponseEntity.ok(vehicles);
    }

    /**
     * The function retrieves a vehicle by its ID and returns it in a ResponseEntity, handling any exceptions that may
     * occur.
     *
     * @param id The "id" parameter is a Long value that represents the unique identifier of a vehicle. It is used to
     * retrieve the vehicle with the specified id from the vehicle service.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{id}")
    //Tested
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Received request to get vehicle with id: {}", id);
        try {
            Vehicle vehicle = vehicleService.findById(id);
            log.info("Fetched vehicle with id: {}", id);
            return ResponseEntity.ok(vehicle);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * This function saves a new vehicle and returns the URI of the saved vehicle.
     *
     * @param vehicleRequest The vehicleRequest parameter is an object of type VehicleRequest. It is annotated with
     * @RequestBody, which means that it will be deserialized from the request body of the HTTP POST request. The
     * VehicleRequest object contains the data needed to create a new vehicle.
     * @return The method is returning a ResponseEntity.
     */
    @PostMapping
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> save(@RequestBody VehicleRequest vehicleRequest) {
        log.info("Received request to save new vehicle");
        try {
            Vehicle savedVehicle = vehicleService.save(vehicleRequest);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedVehicle.getId())
                    .toUri();
            log.info("Created vehicle with id: {}", savedVehicle.getId());
            return ResponseEntity.created(uri).build();
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * This function updates a vehicle with the specified ID using the provided updated fields and returns the updated
     * vehicle.
     *
     * @param id The "id" parameter is a Long value representing the unique identifier of the vehicle that needs to be
     * updated.
     * @param vehicleRequestWithUpdatedFields The parameter "vehicleRequestWithUpdatedFields" is of type VehicleRequest and
     * it represents the updated fields of a vehicle. It is used to pass the updated information for a vehicle to the
     * update method.
     * @return The method is returning a ResponseEntity object.
     */
    @PatchMapping("/{id}")
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody VehicleRequest vehicleRequestWithUpdatedFields) {
        log.info("Received request to update vehicle with id: {}", id);
        try {
            Vehicle updatedVehicle = vehicleService.update(id, vehicleRequestWithUpdatedFields);
            log.info("Updated vehicle with id: {}", id);
            return ResponseEntity.ok(updatedVehicle);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * This function handles the deletion of a vehicle by its ID, with authorization checks and error handling.
     *
     * @param id The "id" parameter in the above code is a Long value that represents the unique identifier of a vehicle.
     * It is used to specify which vehicle should be deleted from the system.
     * @return The method is returning a ResponseEntity. If the deletion is successful, it returns a ResponseEntity with no
     * content and a status code of 204 (no content). If there is an exception (BackendException), it returns a
     * ResponseEntity with the exception as the body and the corresponding HTTP status code.
     */
    @DeleteMapping("/{id}")
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Received request to markReservationAsCanceled vehicle with id {}", id);
        try {
            vehicleService.delete(id);
            log.info("Deleted vehicle with id {}", id);
            return ResponseEntity.noContent().build();
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }
}
