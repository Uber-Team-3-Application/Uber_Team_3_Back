package com.reesen.Reesen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Ride;
import java.util.Date;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    Optional<Ride> findDriverActiveRide(Long driverId);

    Ride withdrawRide(Long id);

    Ride panicPressed(Long id, String reason);

    Ride cancelRide(Long id, String reason);

    Ride endRide(Long id);

    Ride acceptRide(Long id);
}