package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The Address class is a Java entity that represents a physical address and is associated with a Location and a set of
 * Users.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String street;

    @ManyToOne
    @ToString.Exclude
    private Location location;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "address")
    private Set<User> user;
}
