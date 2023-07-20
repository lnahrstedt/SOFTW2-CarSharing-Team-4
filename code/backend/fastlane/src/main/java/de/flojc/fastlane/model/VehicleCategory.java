package de.flojc.fastlane.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

/**
 * The VehicleCategory class is an entity that represents a category of vehicles and includes properties such as an ID,
 * name, and a set of configurations.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCategory {

    @Id
    private Character id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "vehicleCategory")
    private Set<Configuration> configurations;
}