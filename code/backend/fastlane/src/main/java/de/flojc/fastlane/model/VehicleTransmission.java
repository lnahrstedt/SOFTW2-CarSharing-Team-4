package de.flojc.fastlane.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

/**
 * The class "VehicleTransmission" represents a vehicle transmission and includes properties such as id, name, and
 * configurations.
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTransmission {

    @Id
    private Character id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "vehicleTransmission")
    private Set<Configuration> configurations;
}