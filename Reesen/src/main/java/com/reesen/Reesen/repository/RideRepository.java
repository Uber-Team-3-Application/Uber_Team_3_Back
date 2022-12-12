package com.reesen.Reesen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Ride;

import java.time.LocalDateTime;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    public Page<Ride> findAllByDriverId(Long driverId, Pageable page);

    public Page<Ride> findAllByDriverIdAndStartTimeAfterAndEndTimeBefore(
            Long driverId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable page);

    public Page<Ride> findAllByDriverIdAndStartTimeAfter(
            Long driverId,
            LocalDateTime startTime,
            Pageable page);

    public Page<Ride> findAllByDriverIdAndEndTimeBefore(
            Long driverId,
            LocalDateTime endTime,
            Pageable page);
}
