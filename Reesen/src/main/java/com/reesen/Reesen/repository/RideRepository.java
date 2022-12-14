package com.reesen.Reesen.repository;

import com.reesen.Reesen.Enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import com.reesen.Reesen.model.Ride;
import java.util.Date;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    Optional<Ride> findRideByDriverIdAndStatus(Long driverId, RideStatus status);

    public Page<Ride> findAllByDriverId(Long driverId, Pageable page);

    public Page<Ride> findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(
            Long driverId,
            LocalDateTime timeOfStart,
            LocalDateTime timeOfEnd,
            Pageable page);

    public Page<Ride> findAllByDriverIdAndTimeOfStartAfter(
            Long driverId,
            LocalDateTime timeOfStart,
            Pageable page);

    public Page<Ride> findAllByDriverIdAndTimeOfEndBefore(
            Long driverId,
            LocalDateTime timeOfEnd,
            Pageable page);
}
