package de.flojc.fastlane.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.flojc.fastlane.serializer.EntityIdSerializer;
import de.flojc.fastlane.serializer.ReservationStateSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * The Reservation class represents a reservation for a vehicle made by a driver, including details such as price, currency
 * code, start and end date/time, and the current state of the reservation.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double price;
    private String currencyCode;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @ManyToOne
    @ToString.Exclude
    @JsonSerialize(using = ReservationStateSerializer.class)
    private ReservationState reservationState;

    @ManyToOne
    @ToString.Exclude
    @JsonSerialize(using = EntityIdSerializer.class)
    private Vehicle vehicle;

    @ManyToOne
    @ToString.Exclude
    @JsonSerialize(using = EntityIdSerializer.class)
    private Driver driver;
}