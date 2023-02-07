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

import com.reesen.Reesen.dto.ReportDTO;
import com.reesen.Reesen.dto.RideLocationWithTimeDTO;
import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.model.Ride;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations="classpath:application-test-repository.properties")
public class RideRepositoryTest {

    @Autowired
    private RideRepository rideRepository;
    private final Long driverId = 1L;
    private final Long passengerId = 4L;

    @Test
    @DisplayName(value = "Getting all driver rides by driver id")
    public void findAllRidesBy_driverId(){
        Page<Ride> rides = this.rideRepository.findAllByDriverId(driverId, PageRequest.of(0, 10));

        assertTrue(rides.hasContent());
        assertThat(rides.getTotalElements()).isLessThan(11);
        assertEquals(rides.getSize(), 10);


        rides = this.rideRepository.findAllByDriverId(driverId, PageRequest.of(5, 100));
        assertEquals(rides.getNumberOfElements(), 0);

    }

    @Test
    @DisplayName(value = "Getting all driver rides with invalid driver id")
    public void findAllRidesBy_invalidDriverId(){
        Page<Ride> rides = this.rideRepository.findAllByDriverId(123123L, PageRequest.of(0, 10));

        assertFalse(rides.hasContent());
        assertEquals(0, rides.getTotalElements());


    }

    @Test
    @DisplayName(value = "Getting driver rides with valid start and end time")
    public void findAllRidesBy_driverIdAnd_timeOfStartAfterAnd_timeOfEndBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date start = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date end = calendar.getTime();


        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(
                driverId, start, end, PageRequest.of(0, 10));

        assertTrue(rides.hasContent());
        assertEquals(10, rides.getSize());
    }
    @Test
    @DisplayName(value = "Getting driver rides with invalid start and end time between")
    public void findZeroRides_withInvalid_startAndEndDateBetween(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date start = calendar.getTime();
        calendar.add(Calendar.YEAR, 2);
        Date end = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(
                driverId, start, end, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }



    @Test
    @DisplayName(value = "Getting driver rides with valid start time")
    public void findAllRidesBy_driverIdAnd_StartTimeAfter() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date start = calendar.getTime();
        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfStartAfter(
                driverId, start, PageRequest.of(0, 10));
        assertTrue(rides.hasContent());
        assertEquals(10, rides.getSize());
    }
    @Test
    @DisplayName(value = "Getting driver rides with invalid start time")
    public void findZeroDriverRides_withInvalid_startTimeAfter(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date start = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfStartAfter(
                driverId, start, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }

    @Test
    @DisplayName(value = "Getting driver rides with valid end time")
    public void findAllRidesBy_driverIdAnd_timeOfEndBefore() {

        Date end = new Date();
        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfEndBefore(
                driverId, end, PageRequest.of(0, 10));
        assertTrue(rides.hasContent());
        assertEquals(10, rides.getSize());
    }

    @Test
    @DisplayName(value = "Getting driver rides with invalid end time")
    public void findZeroRidesBy_driverId_andTimeOfEndBeforeInvalid() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -15);
        Date end = calendar.getTime();
        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfEndBefore(
                driverId, end, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }


    @Test
    @DisplayName(value = "Getting passenger rides with valid between dates")
    public void findAllRidesBy_passengerIdAnd_timeOfStartAfterAnd_timeOfEndBefore(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        Date timeOfStart = calendar.getTime();
        calendar.set(2023, Calendar.DECEMBER, 31, 0, 0, 0);
        Date timeOfEnd = calendar.getTime();


        Page<Ride> rides = rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(
                passengerId, timeOfStart, timeOfEnd, PageRequest.of(0, 10));
        assertTrue(rides.hasContent());
        assertThat(rides.getTotalElements()).isLessThan(11);
        assertThat(rides.getTotalElements()).isGreaterThan(0);

    }
    @Test
    @DisplayName(value = "Getting passenger rides with invalid between dates")
    public void findZeroRidesBy_passengerId_withOldStartAndEndDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2005, Calendar.JANUARY, 1, 0, 0, 0);
        Date timeOfStart = calendar.getTime();
        calendar.set(2007, Calendar.DECEMBER, 31, 0, 0, 0);
        Date timeOfEnd = calendar.getTime();
        Page<Ride> rides = rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(
                passengerId, timeOfStart, timeOfEnd, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }

    @Test
    @DisplayName(value = "Getting passenger rides with valid start time")
    public void findAllRidesBy_passengerIdAnd_timeOfStartAfter(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        Date timeOfStart = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfter(
                passengerId, timeOfStart, PageRequest.of(0, 10));
        assertTrue(rides.hasContent());
        assertThat(rides.getTotalElements()).isLessThan(11);
        assertThat(rides.getTotalElements()).isGreaterThan(0);

    }
    @Test
    @DisplayName(value = "Getting passenger rides with invalid start time")
    public void findZeroRidesBy_passengerId_withOldStart(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.MARCH, 1, 0, 0, 0);
        Date timeOfStart = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfter(
                passengerId, timeOfStart, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }


    @Test
    @DisplayName(value = "Getting passenger rides with valid end time")
    public void findAllRidesBy_passengerIdAnd_timeOfEndBeforeValid(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.MARCH, 1, 0, 0, 0);
        Date timeOfEnd = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllRidesByPassengerIdAndTimeOfEndBefore(
                passengerId, timeOfEnd, PageRequest.of(0, 10));
        assertTrue(rides.hasContent());
        assertThat(rides.getTotalElements()).isLessThan(11);
        assertThat(rides.getTotalElements()).isGreaterThan(0);

    }
    @Test
    @DisplayName(value = "Getting passenger rides with invalid end time")
    public void findZeroRidesBy_passengerIdWith_TimeOfEndBeforeInvalid(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.MARCH, 1, 0, 0, 0);
        Date timeOfEnd = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllRidesByPassengerIdAndTimeOfEndBefore(
                passengerId, timeOfEnd, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }

    @Test
    @DisplayName(value = "Getting rides per day report with valid dates")
    public void getRidesPerDayReportWith_validDatesFromTo(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2023, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();

        List<ReportDTO<Long>> reports = this.rideRepository.getRidesPerDayReport(from, to);

        assertThat(reports.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName(value = "Getting rides per day report with invalid dates")
    public void getRidesPerDayReportWith_invalidDatesFromTo(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2019, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();

        List<ReportDTO<Long>> reports = this.rideRepository.getRidesPerDayReport(from, to);

        assertEquals(0, reports.size());
    }

    @Test
    @DisplayName(value = "Getting vehicle id with valid ride id")
    public void getVehicleTypeId_fromRideId_withValidRideId(){
        Long rideId = 1L;

        Long vehicleId = this.rideRepository.getVehicleTypeId(rideId);
        assertThat(vehicleId).isNotNull();
        assertThat(vehicleId).isLessThan(4L);

    }

    @ParameterizedTest
    @DisplayName(value = "Getting vehicle id with invalid ride id")
    @ValueSource(longs = {0L, 1123L, 50000L})
    public void getVehicleTypeId_fromRideId_withInvalidRideId(Long rideId){

        Long vehicleId = this.rideRepository.getVehicleTypeId(rideId);
        assertThat(vehicleId).isNull();
    }

    @Test
    @DisplayName(value = "Getting driver id with valid ride id")
    public void getDriverId_fromRideId_withValidRideId(){
        Long rideId = 1L;

        Long driverId = this.rideRepository.getDriverIdFromRide(rideId);
        assertThat(driverId).isNotNull();
        assertEquals(1L, driverId);

    }

    @ParameterizedTest
    @DisplayName(value = "Getting driver id with invalid ride id")
    @ValueSource(longs = {0L, 1123L, 50000L})
    public void getDriverId_fromRideId_withInvalidRideId(Long rideId){

        Long driverId = this.rideRepository.getVehicleTypeId(rideId);
        assertThat(driverId).isNull();
    }

    @Test
    @DisplayName(value = "Getting all rides with start time of the ride having start time between two dates")
    public void getAllRidesWith_startTimeBetweenDates(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2023, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();

        List<RideLocationWithTimeDTO> rides = this.rideRepository.getAllRidesWithStartTimeBetween(from, to);


        assertNotEquals(0, rides.size());

    }

    @Test
    @DisplayName(value = "Getting all rides with start time of the ride having start time between two outdated dates")
    public void getAllRidesWith_startTimeBetweenInvalidDates(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2001, Calendar.JANUARY, 1, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2006, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();

        List<RideLocationWithTimeDTO> rides = this.rideRepository.getAllRidesWithStartTimeBetween(from, to);


        assertEquals(0, rides.size());

    }

    @Test
    @DisplayName(value = "Getting all rides with start time of the ride having start time between two dates for specific driver")
    public void getAllRidesWith_startTimeBetweenDatesFor_specificDriver(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2023, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();

        List<RideLocationWithTimeDTO> rides = this.rideRepository.getRidesWithStartTimeBetweenForSpecificDriver(from, to, driverId);


        assertNotEquals(0, rides.size());

    }

    @Test
    @DisplayName(value = "Getting all rides with start time of the ride having start time between two outdated dates for specific driver")
    public void getAllRidesWith_startTimeBetweenInvalidDatesFor_specificDriver(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2001, Calendar.JANUARY, 1, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2006, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();

        List<RideLocationWithTimeDTO> rides = this.rideRepository.getRidesWithStartTimeBetweenForSpecificDriver(from, to, driverId);


        assertEquals(0, rides.size());

    }
    @Test
    @DisplayName(value = "Getting all rides with start time of the ride having start time between two valid dates for invalid driver")
    public void getAllRidesWith_startTimeBetweenDatesFor_invalidDriver() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2001, Calendar.JANUARY, 1, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2023, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();

        List<RideLocationWithTimeDTO> rides = this.rideRepository.getRidesWithStartTimeBetweenForSpecificDriver(from, to, 124124L);

        assertEquals(0, rides.size());

    }

    /** @author Veljko */
    @Test
    @DisplayName("Get rides per day for specific driver with valid dates")
    public void getRidesPerDay_forSpecificDrive_withValidDriversId_withValidDatesFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 10, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2023, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();
        Long driverId = 1L;
        List<ReportDTO<Long>> reports = this.rideRepository.getRidesPerDayForSpecificDriver(from, to, driverId);
        assertThat(reports.size()).isGreaterThan(0);

    }

    /** @author Veljko */
    @ParameterizedTest
    @DisplayName("Get rides per day for specific driver with invalid drivers id and valid dates")
    @ValueSource(longs = {0L, 1235L, -10L})
    public void getRidesPerDay_forSpecificDriver_withInvalidDriversId_andValidDatesFromTo(Long driverId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 10, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2023, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Long>> reports = this.rideRepository.getRidesPerDayForSpecificDriver(from, to, driverId);
        assertThat(reports.size()).isEqualTo(0);
    }


    /** @author Veljko */
    @Test
    @DisplayName("Get rides per day for specific driver with invalid dates")
    public void getRidesPerDay_forSpecificDriver_withValidDriversId_withInvalidDatesFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1989, Calendar.JANUARY, 10, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2003, Calendar.DECEMBER, 31, 0, 0, 0);
        Date to = calendar.getTime();
        Long driverId = 1L;
        List<ReportDTO<Long>> reports = this.rideRepository.getRidesPerDayForSpecificDriver(from, to, driverId);
        assertThat(reports.size()).isEqualTo(0);

    }

    /** @author Veljko */
    @Test
    @DisplayName("Get total cost per day with valid dates from to")
    public void getTotalCostPerDay_withValidDateFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2022, Calendar.JULY, 28, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Double>> reports = this.rideRepository.getTotalCostPerDay(from, to);

        assertThat(reports.get(0).getTotal()).isEqualTo(4042);
        assertThat(reports.get(1).getTotal()).isEqualTo(2045);
        assertThat(reports.size()).isEqualTo(2);
    }

    /** @author Veljko */
    @Test
    @DisplayName("Get total cost per day with invalid dates from to")
    public void getTotalCostPerDay_withInvalidDateFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 25, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2020, Calendar.DECEMBER, 28, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Double>> reports = this.rideRepository.getTotalCostPerDay(from, to);
        assertThat(reports.size()).isEqualTo(0);
    }

    /** @author Veljko */
    @Test
    @DisplayName("Getting total cost per day for driver when driver's id is valid and with valid dates")
    public void getTotalCostPerDayForDriver_withValidDriversId_andValidDateFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2022, Calendar.OCTOBER, 28, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Double>> reports = this.rideRepository.getTotalCostPerDayForDriver(from, to, driverId);

        assertThat(reports.get(0).getTotal()).isEqualTo(4042);
        assertThat(reports.get(1).getTotal()).isEqualTo(903);
        assertThat(reports.size()).isEqualTo(2);

    }


    /** @author Veljko */
    @Test
    @DisplayName("Getting total cost per day for driver when driver's id is valid and with invalid dates")
    public void getTotalCostPerDayForDriver_withValidDriversId_andInvalidDateFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.JULY, 25, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2019, Calendar.OCTOBER, 28, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Double>> reports = this.rideRepository.getTotalCostPerDayForDriver(from, to, driverId);

        assertThat(reports.size()).isEqualTo(0);

    }

    /** @author Veljko */
    @ParameterizedTest
    @DisplayName("Getting total cost per day for driver when driver's id is invalid and with valid dates")
    @ValueSource(longs = {100L, 0L, -10L})
    public void getTotalCostPerDayForDriver_withInValidDriversId_andValidDateFromTo(Long driverId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2022, Calendar.OCTOBER, 28, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Double>> reports = this.rideRepository.getTotalCostPerDayForDriver(from, to, driverId);

        assertThat(reports.size()).isEqualTo(0);

    }


    /** @author Veljko*/
    @Test
    public void getRides_forDriver_withValidDriversId_andValidDateOfStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 26, 0, 0, 0);
        Date from = calendar.getTime();
        Set<Ride> rides = this.rideRepository.getRides(driverId, from);
        assertThat(rides.size()).isEqualTo(5);
    }



    /** @author Veljko*/
    @Test
    public void getRides_forDriver_withValidDriversId_andInvalidDateOfStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JULY, 26, 0, 0, 0);
        Date from = calendar.getTime();
        Set<Ride> rides = this.rideRepository.getRides(driverId, from);
        assertThat(rides.size()).isEqualTo(0);
    }



    /** @author Veljko*/
    @ParameterizedTest
    @ValueSource(longs = {123L, -50L, 0L})
    public void getRides_forDriver_withInvalidDriversId_andValidDateOfStart(Long driverId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 26, 0, 0, 0);
        Date from = calendar.getTime();
        Set<Ride> rides = this.rideRepository.getRides(driverId, from);
        assertThat(rides.size()).isEqualTo(0);
    }

    /** @author Veljko*/
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    public void getReviewsByRideId_withValidRideId(Long rideId) {
        Set<Review> reviews = this.rideRepository.findAllReviewsBySpecificDriverAndRide(rideId);
        assertThat(reviews.size()).isGreaterThan(0);
    }

    /** @author Veljko*/
    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 355L})
    public void getReviewsByRideId_withInvalidRideId(Long rideId) {
        Set<Review> reviews = this.rideRepository.findAllReviewsBySpecificDriverAndRide(rideId);
        assertThat(reviews.size()).isEqualTo(0);
    }


    /** @author Veljko */
    @Test
    public void getTotalCostPerDayForSpecificDriver_withValidDriversId_andValidDateFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2022, Calendar.OCTOBER, 28, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Double>> reports = this.rideRepository.getTotalCostPerDayForDriver(from, to, 2L);

        assertThat(reports.get(0).getTotal()).isEqualTo(2045);
        assertThat(reports.get(1).getTotal()).isEqualTo(1669);
        assertThat(reports.size()).isEqualTo(2);
    }


    /** @author Veljko */
    @Test
    public void getTotalCostPerDayForSpecificDriver_withValidDriversId_andInvalidDateFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.JULY, 25, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2021, Calendar.OCTOBER, 28, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Double>> reports = this.rideRepository.getTotalCostPerDayForDriver(from, to, 2L);

        assertThat(reports.size()).isEqualTo(0);

    }

    /** @author Veljko */
    @ParameterizedTest
    @ValueSource(longs = {100L, 0L, -10L})
    public void getTotalCostPerDayForSpecificDriver_withInValidDriversId_andValidDateFromTo(Long driverId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2022, Calendar.OCTOBER, 28, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Double>> reports = this.rideRepository.getTotalCostPerDayForDriver(from, to, driverId);

        assertThat(reports.size()).isEqualTo(0);

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
