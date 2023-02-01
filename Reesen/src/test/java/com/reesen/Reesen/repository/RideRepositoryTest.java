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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void testFindDriverByRideId() {
        Long id = 1L;
        Driver driver = rideRepository.findDriverByRideId(id);
        assertNotNull(driver);
        assertEquals("mirko@gmail.com", driver.getEmail());
    }

    @Test
    public void testFindPassengerByRideId() {
        Long id = 1L;
        Set<Passenger> passengers = rideRepository.findPassengerByRideId(id);
        assertNotNull(passengers);
        assertEquals(2, passengers.size());
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
    public void testFindAllRidesByPassengerId() {
        Long passengerId = 3L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ride> rides = rideRepository.findAllRidesByPassengerId(passengerId, pageable);
        assertNotNull(rides);
        assertEquals(2, rides.getTotalElements());
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
    public void testFindAllRidesByPassengerIdAndRideStatus() {
        Set<Ride> rides = rideRepository.findAllRidesByPassengerIdAndRideStatus(1L, RideStatus.SCHEDULED);
        Assert.notNull(rides, "The rides should not be null");
    }

    @Test
    public void testUpdateRideStatus() {
        rideRepository.updateRideStatus(1L, RideStatus.STARTED);
        Ride ride = rideRepository.getOne(1L);
        Assert.isTrue(ride.getStatus() == RideStatus.STARTED, "The ride status should be updated to started");
    }

    @Test
    public void testGetRidesPerDayForSpecificPassenger() {
        List<ReportDTO<Long>> reports = rideRepository.getRidesPerDayForSpecificPassenger(new Date(), new Date(), 1L);
        Assert.notNull(reports, "The reports should not be null");
    }

    @Test
    public void testGetRidesWithStartTimeBetweenForSpecificPassenger() {
        List<RideLocationWithTimeDTO> rides = rideRepository.getRidesWithStartTimeBetweenForSpecificPassenger(new Date(), new Date(), 1L);
        Assert.notNull(rides, "The rides should not be null");
    }

    @Test
    public void testGetTotalCostPerDayForSpecificPassenger() {
        List<ReportDTO<Double>> reports = rideRepository.getTotalCostPerDayForSpecificPassenger(new Date(), new Date(), 1L);
        Assert.notNull(reports, "The reports should not be null");
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
