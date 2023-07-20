package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The VehicleBrand class is a Java entity that represents a vehicle brand and contains a set of vehicle models associated
 * with it.
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String brandName;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "vehicleBrand")
    private Set<VehicleModel> vehicleModels;
}