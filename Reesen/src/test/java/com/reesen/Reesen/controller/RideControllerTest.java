package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.VehicleType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideControllerTest {

    private final String BASE_PATH = "http://localhost:8082/api/ride";
    private final String DRIVER_EMAIL = "marko@gmail.com";
    private final String DRIVER_PASSWORD = "Marko123";
    private final Long driverId = 2L;
    private String driverToken = null;

    private final String PASSENGER_EMAIL = "markopreradovic@gmail.com";
    private final String PASSENGER_PASSWORD = "Marko123";
    private final Long passengerId = 4L;
    private String passengerToken = null;


    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MessageSource messageSource;

    private TestRestTemplate driverRestTemplate, passengerRestTemplate;

    HttpHeaders headers = new HttpHeaders();

    @BeforeAll
    public void setInitialData(){
        headers.setContentType(MediaType.APPLICATION_JSON);
        loginAsDriver();
        loginAsPassenger();
        createRestTemplatesForUsers();

    }

    private void loginAsDriver() {
        HttpEntity<LoginDTO> driverLogin = new HttpEntity<>(new LoginDTO(DRIVER_EMAIL, DRIVER_PASSWORD), headers);

        ResponseEntity<TokenDTO> driverResponse = restTemplate
                .exchange("/api/user/login",
                        HttpMethod.POST,
                        driverLogin,
                        new ParameterizedTypeReference<TokenDTO>() {
                        });

        this.driverToken = driverResponse.getBody().getToken();
    }

    private void loginAsPassenger() {
        HttpEntity<LoginDTO> passengerLogin = new HttpEntity<>(new LoginDTO(PASSENGER_EMAIL, PASSENGER_PASSWORD), headers);

        ResponseEntity<TokenDTO> passengerResponse = restTemplate
                .exchange("/api/user/login",
                        HttpMethod.POST,
                        passengerLogin,
                        new ParameterizedTypeReference<TokenDTO>() {
                        });

        this.passengerToken = passengerResponse.getBody().getToken();
    }

    private void createRestTemplatesForUsers() {
        RestTemplateBuilder builder = new RestTemplateBuilder(rt -> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("X-Auth-Token", this.driverToken);
            return execution.execute(request, body);
        }));
        this.driverRestTemplate = new TestRestTemplate(builder);

        RestTemplateBuilder passBuilder = new RestTemplateBuilder(rt -> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("X-Auth-Token", this.passengerToken);
            return execution.execute(request, body);
        }));
        this.passengerRestTemplate = new TestRestTemplate(passBuilder);
    }

    @Test
    @DisplayName("Accepts created ride with valid driver and ride info")
    public void accept_createdRide_withActiveDriver(){
        LocationDTO departure = new LocationDTO("adresa", 19.55, 22.2);
        LocationDTO destination = new LocationDTO("adresa2", 19.22, 22.1);
        RouteDTO route = new RouteDTO(departure, destination);
        LinkedHashSet<RouteDTO> routes = new LinkedHashSet<>();
        routes.add(route);
        Set<UserDTO> users = new HashSet<>();
        CreateRideDTO createRide = new CreateRideDTO(users, routes, "STANDARD", false, false, null);

        ResponseEntity<RideDTO> createResponse = this.passengerRestTemplate.postForEntity(
                BASE_PATH,
                new HttpEntity<>(createRide),
                RideDTO.class
        );

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
//        ride = acceptResponse.getBody();
//        assertNotNull(ride.getDriver());
//        assertNotNull(ride.getStartTime());
//        assertEquals(ride.getDriver().getId(), driverId);
//        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}
