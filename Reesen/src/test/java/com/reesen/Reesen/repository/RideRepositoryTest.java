package com.reesen.Reesen.repository;


import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.dto.ReportDTO;
import com.reesen.Reesen.dto.RideLocationWithTimeDTO;
import com.reesen.Reesen.dto.RideWithVehicleDTO;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;

import com.reesen.Reesen.model.Route;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations="classpath:application-test.properties")
public class RideRepositoryTest {

    @Autowired
    private RideRepository rideRepository;

    @Test
    public void print(){
        //Page<Ride> findAllByDriverId(Long driverId, Pageable page);

        Page<Ride> rides = this.rideRepository.findAllByDriverId(1L, null);
        assertThat(rides).isNotEmpty();
    }

    @Test
    public void testGetLocationsByRide() {
        Long id = 1L;
        LinkedHashSet<Route> locations = rideRepository.getLocationsByRide(id);
        assertNotNull(locations);
        assertEquals(1, locations.size());
    }

    @Test
    public void testGetLocationsByRideWrongId() {
        Long id = 21L;
        LinkedHashSet<Route> locations = rideRepository.getLocationsByRide(id);
        assertEquals(0, locations.size());
    }

    @Test
    public void testFindDriverByRideId() {
        Long id = 1L;
        Driver driver = rideRepository.findDriverByRideId(id);
        assertNotNull(driver);
        assertEquals("mirko@gmail.com", driver.getEmail());
    }

    @Test
    public void testFindDriverByRideIdWrongId() {
        Long id = 20L;
        Driver driver = rideRepository.findDriverByRideId(id);
        assertEquals(null, driver);
    }

    @Test
    public void testFindPassengerByRideId() {
        Long id = 1L;
        Set<Passenger> passengers = rideRepository.findPassengerByRideId(id);
        assertNotNull(passengers);
        assertEquals(2, passengers.size());
    }

    @Test
    public void testFindPassengerByRideIdWrongId() {
        Long id = 21L;
        Set<Passenger> passengers = rideRepository.findPassengerByRideId(id);
        assertEquals(0, passengers.size());
    }

    @Test
    public void testFindRideByDriverIdAndStatus() {
        Long driverId = 1L;
        RideStatus status = RideStatus.ACTIVE;
        Optional<Ride> ride = rideRepository.findRideByDriverIdAndStatus(driverId, status);
        assertNotNull(ride);
        assertEquals(1L, ride.get().getDriver().getId());
    }

    @Test
    public void testFindRideByDriverIdAndStatusWrongId() {
        Long driverId = 21L;
        RideStatus status = RideStatus.ACTIVE;
        Optional<Ride> ride = rideRepository.findRideByDriverIdAndStatus(driverId, status);
        assertTrue(ride.isEmpty());
    }

    @Test
    public void testFindRideByDriverIdAndStatusNoRide() {
        Long driverId = 1L;
        RideStatus status = RideStatus.STARTED;
        Optional<Ride> ride = rideRepository.findRideByDriverIdAndStatus(driverId, status);
        assertTrue(ride.isEmpty());
    }

    @Test
    public void testFindAllRidesByPassengerId() {
        Long passengerId = 3L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ride> rides = rideRepository.findAllRidesByPassengerId(passengerId, pageable);
        assertNotNull(rides);
        assertEquals(2, rides.getTotalElements());
    }

    @Test
    public void testFindAllRidesByPassengerIdWrongId() {
        Long passengerId = 30L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ride> rides = rideRepository.findAllRidesByPassengerId(passengerId, pageable);
        assertEquals(0, rides.getTotalElements());
    }

    @Test
    public void testFindPassengerActiveRide() {
        Long passengerId = 4L;
        RideStatus rideStatus = RideStatus.ACTIVE;
        Ride ride = rideRepository.findPassengerActiveRide(passengerId, rideStatus);
        assertNotNull(ride);
        assertEquals(5L, ride.getId().longValue());
    }

    @Test
    public void testFindPassengerActiveRideWrongId() {
        Long passengerId = 40L;
        RideStatus rideStatus = RideStatus.ACTIVE;
        Ride ride = rideRepository.findPassengerActiveRide(passengerId, rideStatus);
        assertEquals(null, ride);
    }

    @Test
    public void testFindPassengerActiveRideNoRide() {
        Long passengerId = 3L;
        RideStatus rideStatus = RideStatus.ACTIVE;
        Ride ride = rideRepository.findPassengerActiveRide(passengerId, rideStatus);
        assertEquals(null, ride);
    }

    @Test
    public void testFindAllRidesByPassengerIdAndRideStatus() {
        Set<Ride> rides = rideRepository.findAllRidesByPassengerIdAndRideStatus(1L, RideStatus.SCHEDULED);
        Assert.notNull(rides, "The rides should not be null");
    }

    @Test
    public void testFindAllRidesByPassengerIdAndRideStatusWrongId() {
        Set<Ride> rides = rideRepository.findAllRidesByPassengerIdAndRideStatus(30L, RideStatus.SCHEDULED);
        assertTrue(rides.isEmpty());
    }

    @Test
    public void testFindAllRidesByPassengerIdAndRideStatusNoRide() {
        Set<Ride> rides = rideRepository.findAllRidesByPassengerIdAndRideStatus(1L, RideStatus.STARTED);
        assertTrue(rides.isEmpty());
    }

    @Test
    public void testGetRidesPerDayForSpecificPassenger() {
        List<ReportDTO<Long>> reports = rideRepository.getRidesPerDayForSpecificPassenger(new Date(), new Date(), 1L);
        Assert.notNull(reports, "The reports should not be null");
    }

    @Test
    public void testGetRidesPerDayForSpecificPassengerWrongId() {
        List<ReportDTO<Long>> reports = rideRepository.getRidesPerDayForSpecificPassenger(new Date(), new Date(), 20L);
        assertTrue(reports.isEmpty());
    }

    @Test
    public void testGetRidesPerDayForSpecificPassengerWrongDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        List<ReportDTO<Long>> reports = rideRepository.getRidesPerDayForSpecificPassenger(date, date, 1L);
        assertTrue(reports.isEmpty());
    }

    @Test
    public void testGetRidesWithStartTimeBetweenForSpecificPassenger() {
        List<RideLocationWithTimeDTO> rides = rideRepository.getRidesWithStartTimeBetweenForSpecificPassenger(new Date(), new Date(), 1L);
        Assert.notNull(rides, "The rides should not be null");
    }

    @Test
    public void testGetRidesWithStartTimeBetweenForSpecificPassengerWrongId() {
        List<RideLocationWithTimeDTO> rides = rideRepository.getRidesWithStartTimeBetweenForSpecificPassenger(new Date(), new Date(), 20L);
        assertTrue(rides.isEmpty());
    }

    @Test
    public void testGetRidesWithStartTimeBetweenForSpecificPassengerWrongDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        List<RideLocationWithTimeDTO> rides = rideRepository.getRidesWithStartTimeBetweenForSpecificPassenger(date, date, 1L);
        assertTrue(rides.isEmpty());
    }

    @Test
    public void testGetTotalCostPerDayForSpecificPassenger() {
        List<ReportDTO<Double>> reports = rideRepository.getTotalCostPerDayForSpecificPassenger(new Date(), new Date(), 1L);
        Assert.notNull(reports, "The reports should not be null");
    }

    @Test
    public void testGetTotalCostPerDayForSpecificPassengerWrongId() {
        List<ReportDTO<Double>> reports = rideRepository.getTotalCostPerDayForSpecificPassenger(new Date(), new Date(), 21L);
        assertTrue(reports.isEmpty());
    }

    @Test
    public void testGetTotalCostPerDayForSpecificPassengerWrongDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        List<ReportDTO<Double>> reports = rideRepository.getTotalCostPerDayForSpecificPassenger(date, date, 1L);
        assertTrue(reports.isEmpty());
    }

    @Test
    public void testFindAllByScheduledTimeIsNotNullAndStatus() {
        Set<Ride> rides = rideRepository.findAllByScheduledTimeIsNotNullAndStatus(RideStatus.SCHEDULED);
        Assert.notNull(rides, "The rides should not be null");
    }

    @Test
    public void testGetAllActiveRides() {
        List<RideWithVehicleDTO> rides = rideRepository.getAllActiveRides(RideStatus.STARTED, RideStatus.STARTED);
        Assert.notNull(rides, "The rides should not be null");
    }

}
