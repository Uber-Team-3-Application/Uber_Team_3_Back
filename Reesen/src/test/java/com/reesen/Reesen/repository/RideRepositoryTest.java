package com.reesen.Reesen.repository;


import com.reesen.Reesen.dto.ReportDTO;
import com.reesen.Reesen.model.Ride;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations="classpath:application-test.properties")
public class RideRepositoryTest {

    @Autowired
    private RideRepository rideRepository;

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

    @Test
    @DisplayName("Get total cost per day with valid dates from to")
    public void getTotalCostPerDay_withValidDateFromTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(2022, Calendar.JULY, 28, 0, 0, 0);
        Date to = calendar.getTime();
        List<ReportDTO<Double>> reports = this.rideRepository.getTotalCostPerDay(from, to);
        assertThat(reports.get(0).getTotal()).isEqualTo(6087);
    }




}
