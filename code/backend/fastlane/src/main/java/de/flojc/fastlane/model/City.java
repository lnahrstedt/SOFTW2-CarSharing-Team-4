package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The City class represents a city entity with properties such as id, name, locations, and users.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "city")
    private Set<Location> locations;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "placeOfBirth")
    private Set<User> users;
}
