package com.reesen.Reesen.repository;

import com.reesen.Reesen.Enums.RideStatus;

import com.reesen.Reesen.dto.ReportDTO;
import com.reesen.Reesen.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Ride;
import java.util.*;

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


    @Query("select r from Ride r, Passenger p where p.id=:passengerId and p member of r.passengers")
    Page<Ride> findAllRidesByPassengerId(Long passengerId, Pageable page);

    @Query("select r from Ride r, Passenger p " +
            "where p.id=:passengerId " +
            "and " +
            "p member of r.passengers " +
            "and r.timeOfEnd<=:timeOfEnd and r.timeOfStart>=:timeOfStart")
    Page<Ride> findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(
            Long passengerId,
            Date timeOfStart,
            Date timeOfEnd,
            Pageable page);

    @Query("select r from Ride r, Passenger p  " +
            "where p.id=:passengerId " +
            "and " +
            "p member of r.passengers " +
            "and r.timeOfStart>=:timeOfStart")
    Page<Ride> findAllRidesByPassengerIdAndTimeOfStartAfter(
            Long passengerId,
            Date timeOfStart,
            Pageable page);


    @Query("select r from Ride r, Passenger p  " +
            "where p.id=:passengerId " +
            "and " +
            "p member of r.passengers " +
            "and r.timeOfEnd<=:timeOfEnd")
    Page<Ride> findAllRidesByPassengerIdAndTimeOfEndBefore(
            Long passengerId,
            Date timeOfEnd,
            Pageable page);


    @Query("select new com.reesen.Reesen.dto.ReportDTO(r.timeOfStart, count(r)) from Ride r group by r.timeOfStart")
    List<ReportDTO> getRidesPerDayReport();
}
