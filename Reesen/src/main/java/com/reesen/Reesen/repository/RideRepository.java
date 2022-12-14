package com.reesen.Reesen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Ride;
import java.util.Date;

import java.time.LocalDateTime;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
}
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
