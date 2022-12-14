package com.reesen.Reesen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Ride;
import java.util.Date;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    Page<Ride> findAllPassengerRides(Long id, Pageable page);

    Page<Ride> findAllPassengerRidesBefore(Long id, Date to, Pageable page);

    Page<Ride> findAllPassengerRidesAfter(Long id, Date from, Pageable page);

    Page<Ride> findAllPassengerRidesInDateRange(Long id, Date from, Date to, Pageable page);
}