package de.flojc.fastlane.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

/**
 * The class "VehicleFuel" represents a type of fuel for a vehicle and includes properties such as an ID, name, and a set
 * of configurations.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleFuel {

    @Id
    private Character id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "vehicleFuel")
    private Set<Configuration> configurations;
}