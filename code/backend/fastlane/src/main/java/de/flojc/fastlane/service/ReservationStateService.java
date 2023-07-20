package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.ReservationState;
import de.flojc.fastlane.repository.ReservationStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The ReservationStateService class is a service that retrieves a ReservationState object by its name from the
 * ReservationStateRepository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationStateService {

    private final ReservationStateRepository reservationStateRepository;

    /**
     * The function finds a reservation state by its name and returns it, or throws an exception if it is not found.
     *
     * @param reservationStateName The parameter "reservationStateName" is a String that represents the name of a
     * reservation state.
     * @return The method is returning a ReservationState object.
     */
    public ReservationState findReservationStateByName(String reservationStateName) throws BackendException {
        return reservationStateRepository.findReservationStateByNameIgnoreCase(reservationStateName).orElseThrow(() -> new BackendException(ErrorCode.RESERVATION_STATE_NOT_FOUND, reservationStateName));
    }
}
