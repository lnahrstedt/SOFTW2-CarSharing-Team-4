package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.ReservationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// The code snippet is defining a repository interface called `ReservationStateRepository`. This interface extends the
// `JpaRepository` interface, which is a part of the Spring Data JPA framework.
@Repository
public interface ReservationStateRepository extends JpaRepository<ReservationState, Long> {
    Optional<ReservationState> findReservationStateByNameIgnoreCase(String name);

}
