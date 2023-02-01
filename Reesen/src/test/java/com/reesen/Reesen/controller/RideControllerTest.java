package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.LoginDTO;
import com.reesen.Reesen.dto.TokenDTO;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideControllerTest {

    private final String DRIVER_EMAIL = "marko@gmail.com";
    private final String DRIVER_PASSWORD = "Marko123";

    private final String PASSENGER_EMAIL = "markopreradovic@gmail.com";
    private final String PASSENGER_PASSWORD = "Marko123";


    @Autowired
    private TestRestTemplate restTemplate;

    private TokenDTO driverToken;
    private TokenDTO passengerToken;

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


    @Test
    @DisplayName("Should accept created ride")
    public void test_accept(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token", passengerToken.getToken());
    }

}
