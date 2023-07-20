package de.flojc.fastlane.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The Configuration class represents a configuration for a vehicle, including its category, type, transmission, fuel,
 * associated vehicles, and equipment.
 */
@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private VehicleCategory vehicleCategory;

    @ManyToOne
    @ToString.Exclude
    private VehicleType vehicleType;

    @ManyToOne
    @ToString.Exclude
    private VehicleTransmission vehicleTransmission;

    @ManyToOne
    @ToString.Exclude
    private VehicleFuel vehicleFuel;

    @ToString.Exclude
    @OneToMany(mappedBy = "configuration")
    private Set<Vehicle> vehicles;

    @ManyToOne
    @ToString.Exclude
    private Equipment equipment;
}
