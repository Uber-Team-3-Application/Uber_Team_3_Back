package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideControllerTest {

    private final String DRIVER_EMAIL = "marko@gmail.com";
    private final String DRIVER_PASSWORD = "Marko123";
    private final Long driverId = 2L;

    private final String PASSENGER_EMAIL = "markopreradovic@gmail.com";
    private final String PASSENGER_PASSWORD = "Marko123";
    private final Long passengerId = 4L;


    @Autowired
    private TestRestTemplate restTemplate;

    private TokenDTO driverToken;
    private TokenDTO passengerToken;
    private Set<UserDTO> passengers = new HashSet<>();
    private LinkedHashSet<RouteDTO> routes = new LinkedHashSet<>();
    private CreateRideDTO unscheduledRideDTO;
    HttpHeaders headers = new HttpHeaders();

    @BeforeAll
    public void login(){

        ResponseEntity<TokenDTO> driverResponse = restTemplate
                .postForEntity("/api/user/login",
                        new LoginDTO(DRIVER_EMAIL, DRIVER_PASSWORD),
                        TokenDTO.class);
        driverToken = driverResponse.getBody();

        ResponseEntity<TokenDTO> passengerResponse = restTemplate
                .postForEntity("/api/user/login",
                        new LoginDTO(PASSENGER_EMAIL, PASSENGER_PASSWORD),
                        TokenDTO.class);
        passengerToken = passengerResponse.getBody();
    }


    @BeforeEach
    public void setInitialData(){
        unscheduledRideDTO = new CreateRideDTO(
                this.passengers,
                this.routes,
                "STANDARD",
                false,
                false,
                null);
        passengers = new HashSet<>();
        passengers.add(new UserDTO(3L, "nemus@gmail.com"));

        routes = new LinkedHashSet<>();
        LocationDTO departure = new LocationDTO("Radnicka 19, Novi Sad", 19.5124, 45.12312);
        LocationDTO destination = new LocationDTO("Laze Teleckog 2, Novi Sad", 19.5125, 45.12341);
        routes.add(new RouteDTO(departure, destination));
    }
    @Test
    @DisplayName("Accepts created ride with valid driver and ride info")
    public void accept_createdRide_withActiveDriver(){
        headers.clear();
        headers.add("X-Auth-Token", passengerToken.getToken());

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(unscheduledRideDTO, headers);
        ResponseEntity<RideDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideDTO.class
        );
        assertNotNull(response.getBody());
        RideDTO ride = response.getBody();
        assertEquals(1, ride.getLocations().size());
        assertEquals(2, ride.getPassengers().size());
        assertEquals("STANDARD", ride.getVehicleType().toString());
        assertEquals(RideStatus.PENDING, ride.getStatus());
        assertNull(ride.getStartTime());
        assertNull(ride.getDriver());

        headers.remove("X-Auth-Token");
        headers.add("X-Auth-Token", driverToken.getToken());

        ResponseEntity<RideDTO> acceptResponse  = restTemplate.exchange(
                "/api/ride/"+ride.getId()+"/accept",
                HttpMethod.POST,
                entity,
                RideDTO.class
        );
        assertNotNull(acceptResponse);
        ride = acceptResponse.getBody();
        assertNotNull(ride.getDriver());
        assertNotNull(ride.getStartTime());
        assertEquals(ride.getDriver().getId(), driverId);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}
