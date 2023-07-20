package de.flojc.fastlane.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The ReservationState class is an entity that represents the state of a reservation and contains a set of reservations
 * associated with it.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReservationState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "reservationState")
    private Set<Reservation> reservationSet;
}
