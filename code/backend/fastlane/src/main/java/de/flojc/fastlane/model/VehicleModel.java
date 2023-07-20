package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The VehicleModel class represents a specific model of a vehicle, including its name, picture link, associated brand, and
 * a set of vehicles of that model.
 */
@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String modelName;
    private String linkToPicture;

    @ManyToOne
    private VehicleBrand vehicleBrand;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "vehicleModel")
    private Set<Vehicle> vehicleSet;
}