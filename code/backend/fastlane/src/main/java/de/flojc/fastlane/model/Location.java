package de.flojc.fastlane.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * The Location class represents a location with a postal code, associated city, and associated country.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String postalCode;

    @ManyToOne
    @ToString.Exclude
    private City city;

    @ManyToOne
    @ToString.Exclude
    private Country country;
}
