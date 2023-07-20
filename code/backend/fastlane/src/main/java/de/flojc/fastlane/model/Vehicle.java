package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.flojc.fastlane.serializer.ConfigurationSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The Vehicle class represents a vehicle entity with various attributes and relationships to other entities.
 */
@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String numberPlate;

    private Double mileage;
    private Double latitude;
    private Double longitude;
    private Short constructionYear;

    @ManyToOne
    private VehicleModel vehicleModel;

    @ManyToOne
    @ToString.Exclude
    @JsonSerialize(using = ConfigurationSerializer.class)
    private Configuration configuration;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "vehicle")
    private Set<Reservation> reservations;
}