package com.reesen.Reesen.repository;


import com.reesen.Reesen.model.Ride;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations="classpath:application-test.properties")
public class RideRepositoryTest {

    @Autowired
    private RideRepository rideRepository;
    private Long driverId = 1L;
    private Long passengerId = 1L;

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



}
