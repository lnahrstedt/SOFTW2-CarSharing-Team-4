package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// The code snippet is defining a repository interface called `ReservationRepository` in Java. This interface extends the
// `JpaRepository` interface, which is provided by the Spring Data JPA framework.
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationsByDriverId(Long driverId);

    List<Reservation> findReservationsByVehicleId(Long vehicleId);

    Optional<Reservation> findByIdAndReservationStateNameNot(Long id, String reservationState);

    boolean existsReservationByVehicleIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqualAndReservationStateNameNot(
            Long vehicle_id, LocalDateTime startDateTime, LocalDateTime endDateTime, String reservationState_name);
}