package de.flojc.fastlane.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

/**
 * The VehicleType class is an entity that represents a type of vehicle and includes properties such as an ID, name, and a
 * set of configurations.
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleType {

    @Id
    private Character id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "vehicleType")
    private Set<Configuration> configurations;
}