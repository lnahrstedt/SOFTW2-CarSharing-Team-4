package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

/**
 * The Country class is a Java entity that represents a country and includes properties such as code, name, and a set of
 * locations.
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    private String code;
    private String name;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "country")
    private Set<Location> locations;
}