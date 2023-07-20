package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.*;
import de.flojc.fastlane.repository.ReservationRepository;
import de.flojc.fastlane.request.ReservationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * The `ReservationService` class is responsible for managing reservations, including retrieving, creating, updating, and
 * deleting reservations, as well as performing various validation checks.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationStateService reservationStateService;
    private final VehicleService vehicleService;
    private final DriverService driverService;

    /**
     * The function retrieves all reservations from the reservation repository and logs the number of reservations found.
     *
     * @return The method is returning a List of Reservation objects.
     */
    public List<Reservation> findAll() {
        log.debug("Starting retrieval of all reservations...");
        List<Reservation> reservations = reservationRepository.findAll();
        log.debug("Finished retrieval of all reservations - {} reservations found", reservations.size());
        return reservations;
    }


    /**
     * The function retrieves a reservation by its ID, excluding those with a reservation state of "CANCELED", and throws
     * an exception if the reservation is not found.
     *
     * @param id The "id" parameter is of type Long and represents the unique identifier of the reservation that needs to
     * be retrieved.
     * @return The method is returning a Reservation object.
     */
    public Reservation findById(Long id) throws BackendException {
        log.debug("Starting retrieval of reservation with id: {}...", id);
        Reservation reservation = reservationRepository.findByIdAndReservationStateNameNot(id, "CANCELED").orElseThrow(() -> new BackendException(ErrorCode.RESERVATION_NOT_FOUND, id));
        log.debug("Finished retrieval of reservation with id: {}", id);
        return reservation;
    }


    /**
     * The function saves a new reservation to the repository and returns the created reservation.
     *
     * @param reservation The parameter "reservation" is an object of type Reservation, which represents a reservation that
     * needs to be saved to the repository.
     * @return The method is returning the created reservation object.
     */
    public Reservation save(Reservation reservation) {
        log.debug("Starting to save a new reservation to the repository...");
        Reservation createdReservation = reservationRepository.save(reservation);
        log.debug("Finished saving a new reservation to the repository. Id: {}", reservation.getId());
        return createdReservation;
    }

    /**
     * The function updates a reservation with the provided ID and the fields specified in the ReservationRequest object,
     * while also performing various validations.
     *
     * @param id The `id` parameter is the unique identifier of the reservation that needs to be updated.
     * @param reservationWithUpdatedFields The parameter `reservationWithUpdatedFields` is an object of type
     * `ReservationRequest` which contains the updated fields for the reservation.
     * @return The method is returning a Reservation object.
     */
    public Reservation update(Long id, ReservationRequest reservationWithUpdatedFields) throws BackendException {
        log.debug("Starting update of reservation with id: {}...", id);
        reservationWithUpdatedFields.validateFieldsNotBlank();
        validateAuthForUpdate(id);

        if (Objects.nonNull(reservationWithUpdatedFields.getDriverId())) {
            throw new BackendException(ErrorCode.PATCHING_DRIVER_IN_RESERVATION_FORBIDDEN);
        }

        if (Objects.nonNull(reservationWithUpdatedFields.getVehicleId())) {
            throw new BackendException(ErrorCode.PATCHING_VEHICLE_IN_RESERVATION_FORBIDDEN);
        }

        if (Objects.nonNull(reservationWithUpdatedFields.getPrice())) {
            throw new BackendException(ErrorCode.PATCHING_PRICE_IN_RESERVATION_FORBIDDEN);
        }

        if (Objects.nonNull(reservationWithUpdatedFields.getCurrencyCode())) {
            throw new BackendException(ErrorCode.PATCHING_CURRENCY_IN_RESERVATION_FORBIDDEN);
        }

        if (Objects.nonNull(reservationWithUpdatedFields.getStartDateTime()) || Objects.nonNull(reservationWithUpdatedFields.getEndDateTime())) {
            throw new BackendException(ErrorCode.PATCHING_DATES_IN_RESERVATION_FORBIDDEN);
        }

        Reservation reservationToBeUpdated = reservationRepository.findById(id).orElseThrow(() -> new BackendException(ErrorCode.RESERVATION_NOT_FOUND, id));


        if (reservationWithUpdatedFields.getReservationState() != null) {
            String reservationStateName = reservationWithUpdatedFields.getReservationState();
            reservationToBeUpdated.setReservationState(reservationStateService.findReservationStateByName(reservationStateName));
        }

        Reservation reservation = save(reservationToBeUpdated);
        log.debug("Finished updating of reservation with id: {}", id);
        return reservation;
    }

    /**
     * The function marks a reservation as canceled if its start date and time is in the future.
     *
     * @param id The "id" parameter is of type Long and represents the unique identifier of the reservation that needs to
     * be marked as canceled.
     */
    public void markReservationAsCanceled(Long id) throws BackendException {
        log.debug("Starting marking reservation with id: {} as canceled...", id);
        validateAuthForDelete(id);
        Reservation reservation = findById(id);
        if (reservation.getStartDateTime().isAfter(LocalDateTime.now())) {
            reservation.setReservationState(reservationStateService.findReservationStateByName("CANCELED"));
            save(reservation);
        }
        log.debug("Finished marking reservation with id: {} as canceled", id);
    }

    /**
     * The function deletes all reservations for a driver with a given ID.
     *
     * @param reservations A list of Reservation objects that need to be deleted.
     * @param id The id parameter represents the unique identifier of the driver for whom the reservations need to be
     * deleted.
     */
    public void deleteAll(List<Reservation> reservations, long id) {
        log.debug("Starting deleting reservations for the driver with id: {}...", id);
        reservationRepository.deleteAll(reservations);
        log.debug("Finished deleting reservations for the driver with id: {}", id);
    }

    /**
     * The function retrieves reservations by driver ID.
     *
     * @param driverId The driverId parameter is a long value that represents the unique identifier of a driver.
     * @return The method is returning a List of Reservation objects.
     */
    public List<Reservation> getReservationsByDriverId(long driverId) throws BackendException {
        log.debug("Starting retrieval of reservations from driver with id: {}...", driverId);
        driverService.findById(driverId);
        List<Reservation> reservations = reservationRepository.findReservationsByDriverId(driverId);
        log.debug("Finished retrieval of reservations from driver with id: {}", driverId);
        return reservations;
    }

    /**
     * The function retrieves reservations by a given vehicle ID.
     *
     * @param vehicleId The vehicleId parameter is a long value that represents the unique identifier of a vehicle.
     * @return The method is returning a List of Reservation objects.
     */
    public List<Reservation> getReservationsByVehicleId(long vehicleId) throws BackendException {
        log.debug("Starting retrieval of reservations by vehicle id: {}...", vehicleId);
        vehicleService.findById(vehicleId);
        List<Reservation> reservations = reservationRepository.findReservationsByVehicleId(vehicleId);
        log.debug("Finished retrieval of reservations by vehicle id: {}", vehicleId);
        return reservations;
    }

    /**
     * The function creates a reservation by validating and saving a reservation request.
     *
     * @param newReservation The newReservation parameter is an object of type ReservationRequest, which contains the
     * information needed to create a new reservation.
     * @return The method is returning a Reservation object.
     */
    public Reservation createViaReservationRequest(ReservationRequest newReservation) throws BackendException {
        log.debug("Starting to save reservation via reservation request...");
        validateReservationRequestOnSave(newReservation);
        Reservation savedReservation = save(convertReservationRequestToReservation(newReservation));
        log.debug("Finished creating reservation via reservation request");
        return savedReservation;
    }

    /**
     * The function validates a reservation request by checking if the fields are not null or blank, validating the
     * driver's authentication, and checking the price and date availability.
     *
     * @param reservationRequest The reservationRequest parameter is an object of type ReservationRequest.
     */
    private void validateReservationRequestOnSave(ReservationRequest reservationRequest) throws BackendException {
        log.debug("Starting to validate incoming reservation request: {}...", reservationRequest);
        reservationRequest.validateFieldsNotNullOrBlank();
        validateAuth(reservationRequest.getDriverId());
        validatePriceDateAvailability(reservationRequest);
        log.debug("Finished validating reservation request {} is valid", reservationRequest);
    }

    /**
     * The function validates the price, date, and availability of a vehicle for a reservation request.
     *
     * @param reservationRequest An object that contains information about a reservation request, including the vehicle ID,
     * start date and time, and end date and time.
     */
    private void validatePriceDateAvailability(ReservationRequest reservationRequest) throws BackendException {
        Long vehicleId = reservationRequest.getVehicleId();
        LocalDateTime startDateTime = reservationRequest.getStartDateTime();
        LocalDateTime endDateTime = reservationRequest.getEndDateTime();

        validatePrice(reservationRequest);
        validateDate(startDateTime, endDateTime);
        validateVehicleAvailable(vehicleId, startDateTime, endDateTime);
    }

    /**
     * The function validates the authentication of a user by checking if the user is a member and if the user has access
     * to a specific driver.
     *
     * @param id The "id" parameter is the identifier of a driver.
     */
    private void validateAuth(Long id) throws BackendException {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (account.getAccountType().getName().equals("MEMBER")) {
            Driver driver = driverService.findById(id);
            if (!Objects.equals(account.getUser().getId(), driver.getUser().getId())) {
                throw new AccessDeniedException("");
            }
        }
    }

    /**
     * The function validates the authorization for deleting a reservation based on the account type and the relationship
     * between the account and the reservation's driver.
     *
     * @param id The "id" parameter is a Long value representing the unique identifier of a reservation.
     */
    private void validateAuthForDelete(Long id) throws BackendException {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (account.getAccountType().getName().equals("MEMBER")) {
            Reservation reservation = findById(id);
            if (!Objects.equals(account.getUser().getId(), reservation.getDriver().getUser().getId())) {
                throw new AccessDeniedException("");
            }
        }
    }

    /**
     * The function validates the authentication for updating a reservation by checking if the logged-in account is the
     * owner of the reservation.
     *
     * @param id The "id" parameter is the identifier of a reservation that needs to be validated for authorization before
     * updating.
     */
    private void validateAuthForUpdate(Long id) throws BackendException {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (account.getAccountType().getName().equals("MEMBER")) {
            Reservation reservation = findById(id);
            long userIdOfReservation = reservation.getDriver().getUser().getId();
            long userIdOfLoggedInAccount = account.getUser().getId();
            if (userIdOfReservation != userIdOfLoggedInAccount) {
                throw new AccessDeniedException("");
            }
        }
    }

    /**
     * The function `validatePrice` calculates the price for a reservation request based on the driver's fare type and
     * duration, and throws an exception if the calculated price does not match the provided price.
     *
     * @param reservationRequest The reservationRequest parameter is an object that contains information about a
     * reservation, such as the driver ID, start and end date/time, and the price.
     */
    private void validatePrice(ReservationRequest reservationRequest) throws BackendException {
        double calculatedPrice;
        Driver driver = driverService.findById(reservationRequest.getDriverId());
        FareType fareType = driver.getFareType();
        LocalDateTime start = reservationRequest.getStartDateTime();
        LocalDateTime end = reservationRequest.getEndDateTime();
        Duration difference = Duration.between(start, end);
        if (difference.toMinutes() <= 60) {
            calculatedPrice = fareType.getPrice();
        } else {
            if (difference.toMinutes() % 60 != 0) {
                calculatedPrice = (difference.toHours() + 1) * fareType.getPrice();
            } else {
                calculatedPrice = difference.toHours() * fareType.getPrice();
            }
        }
        if (reservationRequest.getPrice() != calculatedPrice) {
            throw new BackendException(ErrorCode.RESERVATION_PRICE_DOES_NOT_MATCH, reservationRequest.getPrice());
        }
    }

    /**
     * The function validates if the start date and time is before the end date and time, otherwise it throws a
     * BackendException with an error code and the invalid start and end date and time.
     *
     * @param startDateTime The startDateTime parameter represents the starting date and time of a period.
     * @param endDateTime The endDateTime parameter represents the date and time that marks the end of a period or
     * interval.
     */
    private void validateDate(LocalDateTime startDateTime, LocalDateTime endDateTime) throws BackendException {
        if (!startDateTime.isBefore(endDateTime)) {
            throw new BackendException(ErrorCode.INVALID_PERIOD, startDateTime, endDateTime);
        }
    }

    /**
     * The function validates if a vehicle is available for reservation during a specified time period.
     *
     * @param vehicleId The unique identifier of the vehicle that needs to be validated for availability.
     * @param startDateTime The startDateTime parameter represents the date and time when the vehicle reservation is
     * supposed to start. It is of type LocalDateTime, which is a class in Java that represents a date and time without a
     * time zone.
     * @param endDateTime The endDateTime parameter represents the date and time when the vehicle reservation ends.
     */
    private void validateVehicleAvailable(Long vehicleId, LocalDateTime startDateTime, LocalDateTime endDateTime) throws BackendException {
        boolean result = isVehicleReservable(vehicleId, startDateTime, endDateTime);

        if (!result) {
            throw new BackendException(ErrorCode.VEHICLE_ALREADY_RESERVED, vehicleId, startDateTime, endDateTime);
        }
    }

    /**
     * The function converts a list of reservation requests into a list of reservations and saves them.
     *
     * @param reservationRequests A list of ReservationRequest objects.
     */
    public void convertListOfReservationRequestToListOfReservation(List<ReservationRequest> reservationRequests) throws BackendException {
        for (ReservationRequest reservationRequest : reservationRequests) {
            save(convertReservationRequestToReservation(reservationRequest));
        }
    }

    /**
     * The function checks if a vehicle is reservable within a given time frame, excluding any reservations in the
     * "CANCELED" state.
     *
     * @param vehicleId The vehicleId parameter is the unique identifier of the vehicle for which we want to check if it is
     * reservable.
     * @param startDateTime The start date and time of the reservation.
     * @param endDateTime The endDateTime parameter represents the date and time when the reservation ends.
     * @return The method is returning a boolean value.
     */
    private boolean isVehicleReservable(Long vehicleId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return !reservationRepository.existsReservationByVehicleIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqualAndReservationStateNameNot(vehicleId, startDateTime.minusMinutes(30), endDateTime.plusMinutes(30), "CANCELED");
    }

    /**
     * The function converts a reservation request object into a reservation object by retrieving the necessary information
     * from other services and building the reservation object.
     *
     * @param reservationRequest The reservationRequest parameter is an object of type ReservationRequest. It contains
     * information about a reservation, such as the driver ID, vehicle ID, price, currency code, reservation state, start
     * date and time, and end date and time.
     * @return The method is returning a Reservation object.
     */
    private Reservation convertReservationRequestToReservation(ReservationRequest reservationRequest) throws BackendException {
        log.debug("Starting to convert reservation request to reservation...");

        Long driverId = reservationRequest.getDriverId();
        Long vehicleId = reservationRequest.getVehicleId();

        Driver driver = driverService.findById(driverId);
        Vehicle vehicle = vehicleService.findById(vehicleId);

        double price = reservationRequest.getPrice();

        ReservationState reservationState = reservationStateService.findReservationStateByName(reservationRequest.getReservationState());

        Reservation reservation = Reservation.builder()
                .driver(driver)
                .vehicle(vehicle)
                .price(price)
                .currencyCode(reservationRequest.getCurrencyCode())
                .reservationState(reservationState)
                .startDateTime(reservationRequest.getStartDateTime())
                .endDateTime(reservationRequest.getEndDateTime())
                .build();

        log.debug("Finished converting reservation request to reservation");
        return reservation;
    }
}