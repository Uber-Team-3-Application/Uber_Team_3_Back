package com.reesen.Reesen.repository;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import com.reesen.Reesen.model.Ride;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    @Query("select r.locations from Ride r where r.id = :id")
    Set<Route> getLocationsByRide(Long id);

    Optional<Ride> findRideByDriverIdAndStatus(Long driverId, RideStatus status);
    Page<Ride> findAllByDriverId(Long driverId, Pageable page);

    Page<Ride> findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(
            Long driverId,
            Date timeOfStart,
            Date timeOfEnd,
            Pageable page);

     Page<Ride> findAllByDriverIdAndTimeOfStartAfter(
            Long driverId,
            Date timeOfStart,
            Pageable page);

     Page<Ride> findAllByDriverIdAndTimeOfEndBefore(
            Long driverId,
            Date timeOfEnd,
            Pageable page);


}
