package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.model.Reservation;
import de.flojc.fastlane.request.ReservationRequest;
import de.flojc.fastlane.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * The ReservationController class is a REST controller that handles requests related to reservations, including
 * retrieving, creating, updating, and deleting reservations.
 */
@Slf4j
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * The function returns all reservations and logs the number of reservations fetched.
     *
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping
    //Tested
    public ResponseEntity<?> getAll() {
        log.info("Received request to get all reservations");
        List<Reservation> reservations = reservationService.findAll();
        log.info("Fetched {} reservations", reservations.size());
        return ResponseEntity.ok(reservations);
    }

    /**
     * The function retrieves a reservation by its ID and returns it in a ResponseEntity, handling any backend exceptions
     * that may occur.
     *
     * @param id The "id" parameter is a Long value that represents the unique identifier of a reservation. It is used to
     * retrieve a specific reservation from the database.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{id}")
    //Tested
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Received request to get reservation with id: {}", id);
        try {
            Reservation reservation = reservationService.findById(id);
            log.info("Fetched reservation with id: {}", id);
            return ResponseEntity.ok(reservation);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * The function retrieves reservations for a driver based on their driverId and returns them in a ResponseEntity.
     *
     * @param driverId The `driverId` parameter is a `Long` value that represents the unique identifier of a driver. It is
     * used to retrieve reservations associated with the specified driver.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/driver/{driverId}")
    //Tested
    public ResponseEntity<?> getByDriverId(@PathVariable Long driverId) {
        log.info("Received request to get reservations for driver with id: {}", driverId);
        try {
            List<Reservation> reservations = reservationService.getReservationsByDriverId(driverId);
            log.info("Fetched {} reservations for driver with id: {}", reservations.size(), driverId);
            return ResponseEntity.ok(reservations);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * The function retrieves reservations by vehicle ID and returns them in a ResponseEntity.
     *
     * @param vehicleId The vehicleId is a path variable that is passed in the URL. It represents the unique identifier of
     * a vehicle.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/vehicle/{vehicleId}")
    //Tested
    public ResponseEntity<?> getByVehicleId(@PathVariable Long vehicleId) {
        log.info("Received request to get reservations by vehicle id: {}", vehicleId);
        try {
            List<Reservation> reservations = reservationService.getReservationsByVehicleId(vehicleId);
            log.info("Fetched {} reservations for vehicle id: {}", reservations.size(), vehicleId);
            return ResponseEntity.ok(reservations);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * This function saves a new reservation by creating a Reservation object from the ReservationRequest and returns a
     * ResponseEntity with the appropriate status code and location header.
     *
     * @param newReservation The parameter `newReservation` is of type `ReservationRequest`. It is annotated with
     * `@RequestBody`, which means that the data for this parameter will be obtained from the request body of the HTTP POST
     * request.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping
    //Tested
    public ResponseEntity<?> save(@RequestBody ReservationRequest newReservation) {
        log.info("Received request to save new reservation");
        try {
            Reservation createdReservation = reservationService.createViaReservationRequest(newReservation);
            log.info("Created reservation with id {}", createdReservation.getId());
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdReservation.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * This function updates a reservation with the given ID using the provided updated fields and returns the updated
     * reservation.
     *
     * @param id The "id" parameter is a Long value that represents the unique identifier of the reservation that needs to
     * be updated. It is extracted from the path variable in the URL.
     * @param reservationWithUpdatedFields The parameter `reservationWithUpdatedFields` is of type `ReservationRequest`. It
     * is used to pass the updated fields of a reservation that needs to be updated.
     * @return The method is returning a ResponseEntity object.
     */
    @PatchMapping("/{id}")
    //Tested
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ReservationRequest reservationWithUpdatedFields) {
        log.info("Received request to update reservation with id: {}", id);
        try {
            Reservation updatedReservation = reservationService.update(id, reservationWithUpdatedFields);
            log.info("Updated reservation with id: {}", id);
            return ResponseEntity.ok(updatedReservation);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * The above function is a DELETE endpoint in a Java application that marks a reservation as canceled and returns a
     * response entity.
     *
     * @param id The "id" parameter in the above code represents the unique identifier of a reservation that needs to be
     * deleted.
     * @return The method is returning a ResponseEntity object.
     */
    @DeleteMapping("/{id}")
    //Tested
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Received request to markReservationAsCanceled reservation with id: {}", id);
        try {
            reservationService.markReservationAsCanceled(id);
            log.info("Deleted reservation with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }
}
