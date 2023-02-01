package com.reesen.Reesen.repository;


import com.reesen.Reesen.model.Ride;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

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

}
