package de.flojc.fastlane.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The FareType class is an entity class that represents a type of fare, including its name, price, and a set of associated
 * drivers.
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FareType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private double price;

    @ToString.Exclude
    @OneToMany(mappedBy = "fareType")
    private Set<Driver> accountSet;
}
