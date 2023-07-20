package de.flojc.fastlane.request;

import lombok.*;

import java.time.LocalDateTime;

/**
 * The class ReservationRequest is a subclass of Request and represents a request for a reservation with various properties
 * such as vehicleId, driverId, price, currencyCode, startDateTime, endDateTime, and reservationState.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest extends Request {

    protected Long vehicleId;
    protected Long driverId;
    protected Double price;
    protected String currencyCode;
    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;
    protected String reservationState;

}