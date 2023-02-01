package com.reesen.Reesen.repository;


import com.reesen.Reesen.dto.ReportDTO;
import com.reesen.Reesen.model.Ride;

import org.junit.jupiter.api.Test;
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
    private Long driverId = 1L;
    private Long passengerId = 4L;

    @Test
    public void findAllRidesBy_driverId(){
        Page<Ride> rides = this.rideRepository.findAllByDriverId(driverId, PageRequest.of(0, 10));


        assertTrue(rides.hasContent());
        assertThat(rides.getTotalElements()).isLessThan(11);
        assertEquals(rides.getSize(), 10);


        rides = this.rideRepository.findAllByDriverId(driverId, PageRequest.of(5, 100));
        assertEquals(rides.getNumberOfElements(), 0);

    }

    @Test
    public void findAllRidesBy_driverIdAnd_timeOfStartAfterAnd_timeOfEndBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date start = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date end = calendar.getTime();


        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(
                1L, start, end, PageRequest.of(0, 10));

        assertTrue(rides.hasContent());
        assertEquals(10, rides.getSize());
    }
    @Test
    public void findZeroRides_withInvalid_startAndEndDateBetween(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date start = calendar.getTime();
        calendar.add(Calendar.YEAR, 2);
        Date end = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(
                1L, start, end, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }



    @Test
    public void findAllRidesBy_driverIdAnd_StartTimeAfter() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date start = calendar.getTime();
        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfStartAfter(
                1L, start, PageRequest.of(0, 10));
        assertTrue(rides.hasContent());
        assertEquals(10, rides.getSize());
    }
    @Test
    public void findZeroRides_withInvalid_startTimeAfter(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date start = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfStartAfter(
                1L, start, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }

    @Test
    public void findAllRidesBy_driverIdAnd_timeOfEndBefore() {

        Date end = new Date();
        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfEndBefore(
                1L, end, PageRequest.of(0, 10));
        assertTrue(rides.hasContent());
        assertEquals(10, rides.getSize());
    }

    @Test
    public void findZeroRidesBy_driverId_andTimeOfEndBeforeInvalid() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -15);
        Date end = calendar.getTime();
        Page<Ride> rides = rideRepository.findAllByDriverIdAndTimeOfEndBefore(
                1L, end, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }


    @Test
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
    public void findZeroRidesBy_passengerId_withOldStart(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.MARCH, 1, 0, 0, 0);
        Date timeOfStart = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfter(
                passengerId, timeOfStart, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }


    @Test
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
    public void findZeroRidesBy_passengerIdWith_TimeOfEndBeforeInvalid(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.MARCH, 1, 0, 0, 0);
        Date timeOfEnd = calendar.getTime();

        Page<Ride> rides = rideRepository.findAllRidesByPassengerIdAndTimeOfEndBefore(
                passengerId, timeOfEnd, PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }

    @Test
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
    public void getVehicleTypeId_fromRideId_withValidRideId(){
        Long rideId = 1L;

        Long vehicleId = this.rideRepository.getVehicleTypeId(rideId);
        assertThat(vehicleId).isNotNull();
        assertThat(vehicleId).isLessThan(4L);

    }


}
