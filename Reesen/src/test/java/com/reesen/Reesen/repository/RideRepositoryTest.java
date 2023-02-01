package com.reesen.Reesen.repository;


import com.reesen.Reesen.dto.ReportDTO;
import com.reesen.Reesen.dto.RideLocationWithTimeDTO;
import com.reesen.Reesen.model.Ride;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations="classpath:application-test.properties")
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




}
