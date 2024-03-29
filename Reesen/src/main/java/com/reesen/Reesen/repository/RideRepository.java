package com.reesen.Reesen.repository;

import com.reesen.Reesen.Enums.RideStatus;

import com.reesen.Reesen.dto.ReportDTO;
import com.reesen.Reesen.dto.RideLocationWithTimeDTO;
import com.reesen.Reesen.dto.RideWithVehicleDTO;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Ride;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {


    // TODO: JELENA
    @Query("select r.locations from Ride r where r.id=:id")
    LinkedHashSet<Route> getLocationsByRide(Long id);

    @Query("select r.driver from Ride r where r.id=:id")
    Driver findDriverByRideId(Long id);

    @Query("select r.passengers from Ride r where r.id=:id")
    Set<Passenger> findPassengerByRideId(Long id);


    // TODO ---->: JELENA

    // TODO: JELENA
    Optional<Ride> findRideByDriverIdAndStatus(Long driverId, RideStatus status);
    @Query("select r from Ride r, Passenger p  " +
            "where p.id=:passengerId " +
            "and " +
            "p member of r.passengers " +
            "and r.scheduledTime>=:scheduledTime " +
            "and r.driver.id=:driverId " +
            "and r.status=:rideStatus")
    Set<Ride> findAllRidesByDriverIdAndPassengerIdAndScheduledTimeBeforeAndStatus(Long driverId, Long passengerId, LocalDateTime scheduledTime, RideStatus rideStatus);
    // TODO ---->: JELENA

    // TODO: VUGA
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
     //TODO: ---> VUGA


    // TODO: JELENA
    @Query("select r from Ride r, Passenger p where p.id=:passengerId and p member of r.passengers")
    Page<Ride> findAllRidesByPassengerId(Long passengerId, Pageable page);
    //TODO: ----->JELENA

    //TODO : VUGA
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


    @Query("select new com.reesen.Reesen.dto.ReportDTO(r.timeOfStart, count(r)) " +
            "from Ride r " +
            "where r.timeOfStart BETWEEN :from and :to " +
            "group by r.timeOfStart " +
            "order by r.timeOfStart asc")
    List<ReportDTO<Long>> getRidesPerDayReport(Date from, Date to);
    //TODO : VUGA

    // TODO : VELJA

    @Query("select new com.reesen.Reesen.dto.ReportDTO(r.timeOfStart, count(r)) " +
            "from Ride r " +
            "where r.driver.id=:driverId and " +
            "r.timeOfStart BETWEEN :from and :to " +
            "group by r.timeOfStart " +
            "order by r.timeOfStart asc")
    List<ReportDTO<Long>> getRidesPerDayForSpecificDriver(Date from, Date to, Long driverId);

    @Query("select new com.reesen.Reesen.dto.ReportDTO(r.timeOfStart, sum(r.totalPrice))" +
            " from Ride r " +
            "where r.timeOfStart BETWEEN :from and :to " +
            "group by r.timeOfStart " +
            "order by r.timeOfStart asc")
    List<ReportDTO<Double>> getTotalCostPerDay(Date from, Date to);

    @Query("select new com.reesen.Reesen.dto.ReportDTO(r.timeOfStart, sum(r.totalPrice))" +
            " from Ride r " +
            " where r.driver.id=:driverId and " +
            "r.timeOfStart BETWEEN :from and :to " +
            "group by r.timeOfStart " +
            "order by r.timeOfStart asc")
    List<ReportDTO<Double>> getTotalCostPerDayForDriver(Date from, Date to, Long driverId);


    @Query("select new com.reesen.Reesen.dto.ReportDTO(r.timeOfStart, sum(r.totalPrice))" +
            " from Ride r " +
            "where r.driver.id=:driverId and " +
            "r.timeOfStart BETWEEN :from and :to " +
            "group by r.timeOfStart " +
            "order by r.timeOfStart asc")
    List<ReportDTO<Double>> getTotalCostPerDayForSpecificDriver(Date from, Date to, Long driverId);

    @Query("select r from Ride r where r.driver.id=:driverId and r.timeOfStart >= :date")
    Set<Ride> getRides(Long driverId, Date date);

    // TODO: ---->: VEKSON

    // TODO: VUGA
    @Query("select new " +
            "com.reesen.Reesen.dto.RideLocationWithTimeDTO(r.id, r.timeOfStart)" +
            " from Ride r " +
            "where r.timeOfStart BETWEEN :from and :to " +
            "order by r.timeOfStart asc")
    List<RideLocationWithTimeDTO> getAllRidesWithStartTimeBetween(Date from, Date to);

    @Query("select new " +
            "com.reesen.Reesen.dto.RideLocationWithTimeDTO(r.id, r.timeOfStart)" +
            " from Ride r " +
            "where r.driver.id=:driverId and " +
            "r.timeOfStart BETWEEN :from and :to " +
            "order by r.timeOfStart asc")
    List<RideLocationWithTimeDTO> getRidesWithStartTimeBetweenForSpecificDriver(Date from, Date to, Long driverId);


    @Query("select r.driver.id from Ride r where r.id=:id")
    Long getDriverIdFromRide(Long id);

    @Query("select r.vehicleType.id from Ride r where r.id=:id")
    Long getVehicleTypeId(Long id);
    // TODO ---->: VUGA


    //TODO JELENA
    @Query("select r from Ride r, Passenger p  " +
            "where p.id=:passengerId " +
            "and " +
            "p member of r.passengers " +
            "and r.status=:rideStatus")
    Ride findPassengerActiveRide(Long passengerId, RideStatus rideStatus);
    // TODO: ---> : JELENA

    // TODO: VELJA
    @Query("select r.review from Ride r " +
            "where r.id=:rideId")
    Set<Review> findAllReviewsBySpecificDriverAndRide(Long rideId);

    List<Ride> findAll();
    // TODO: ---: VELJA


    // TODO: JELENA
    @Query("select r from Ride r, Passenger p  " +
            "where p.id=:passengerId " +
            "and " +
            "p member of r.passengers " +
            "and r.status=:rideStatus")
    Set<Ride> findAllRidesByPassengerIdAndRideStatus(Long passengerId, RideStatus rideStatus);

    @Transactional
    @Modifying
    @Query("update Ride p set p.status=:rideStatus where p.id=:id")
    void updateRideStatus(Long id, RideStatus rideStatus);

    @Query("select new com.reesen.Reesen.dto.ReportDTO(r.timeOfStart, count(r)) " +
            "from Ride r inner join Passenger p on p member of r.passengers " +
            "where p.id=:passengerId and " +
            "r.timeOfStart BETWEEN :from and :to " +
            "group by r.timeOfStart " +
            "order by r.timeOfStart asc")
    List<ReportDTO<Long>> getRidesPerDayForSpecificPassenger(Date from, Date to, long passengerId);

    @Query("select new " +
            "com.reesen.Reesen.dto.RideLocationWithTimeDTO(r.id, r.timeOfStart)" +
            " from Ride r inner join Passenger p on p member of r.passengers " +
            "where p.id=:passengerId and " +
            "r.timeOfStart BETWEEN :from and :to " +
            "order by r.timeOfStart asc")
    List<RideLocationWithTimeDTO> getRidesWithStartTimeBetweenForSpecificPassenger(Date from, Date to, long passengerId);


    @Query("select new com.reesen.Reesen.dto.ReportDTO(r.timeOfStart, sum(r.totalPrice))" +
            " from Ride r inner join Passenger p on p member of r.passengers " +
            "where p.id=:passengerId and " +
            "r.timeOfStart BETWEEN :from and :to " +
            "group by r.timeOfStart " +
            "order by r.timeOfStart asc")
    List<ReportDTO<Double>> getTotalCostPerDayForSpecificPassenger(Date from, Date to, long passengerId);

    @Query("select r from Ride r where r.status=:status")
    Set<Ride> findAllByScheduledTimeIsNotNullAndStatus(RideStatus status);

    @Query("select new com.reesen.Reesen.dto.RideWithVehicleDTO(r.id, v.id, l.latitude, l.longitude, l.address) from Ride r inner join Driver d " +
            "on r.driver.id = d.id inner join Vehicle v " +
            "on d.vehicle.id = v.id inner join Location  l " +
            "on v.currentLocation.id = l.id " +
            "where r.status=:status or r.status=:started")
    List<RideWithVehicleDTO> getAllActiveRides(RideStatus status, RideStatus started);

    // TODO: ---: JELENA



}
