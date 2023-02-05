package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.ErrorResponseMessage;
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

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        ResponseEntity<RideDTO> acceptResponse = acceptRide(createResponse);

        assertNotNull(acceptResponse.getBody());
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());
        RideDTO ride = acceptResponse.getBody();
        assertNotNull(ride.getDriver());
        assertEquals(1, ride.getPassengers().size());
        assertNull(ride.getRejection());
        assertNull(ride.getScheduledTime());
        assertEquals("ACCEPTED", ride.getStatus().toString());

        // withdraw ride so that it doesn't mess up the other tests
        ResponseEntity<RideDTO> withdraw = cancelRide(ride);
        assertEquals(HttpStatus.OK, withdraw.getStatusCode());
    }


    @Test
    @DisplayName("Tries to accept non existing ride")
    public void accept_nonExistingRide(){

        ResponseEntity<String> acceptResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + 1234 + "/accept",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals("Ride does not exist!", acceptResponse.getBody());
        assertEquals(HttpStatus.NOT_FOUND, acceptResponse.getStatusCode());

    }

    @Test
    @DisplayName("Accept ride with invalid ride id")
    public void doesntAcceptRide_BadRequest_invalidId(){

        ResponseEntity<String> acceptResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + 0 + "/accept",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, acceptResponse.getStatusCode());

    }

    @Test
    @DisplayName("Accept ride with invalid ride Status - NOT PENDING")
    public void doesntAcceptRide_WithStatus_DifferentThatPending(){

        ResponseEntity<ErrorResponseMessage> acceptResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + 5 + "/accept",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<ErrorResponseMessage>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, acceptResponse.getStatusCode());
        assertEquals("Cannot accept a ride that is not in status PENDING!", acceptResponse.getBody().getMessage());
    }

    @Test
    @DisplayName("Accept Ride as Invalid User - Not a Driver")
    public void doesntAcceptRide_asInvalidUser(){

        ResponseEntity<String> acceptResponse = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 5 + "/accept",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, acceptResponse.getStatusCode());
        assertEquals("Access Denied", acceptResponse.getBody());
    }

    @Test
    @DisplayName("Accept Ride with no Token Present")
    public void doesntAcceptRide_withNoTokenPresent(){

        ResponseEntity<String> acceptResponse = this.restTemplate.exchange(
                BASE_PATH + "/5/accept",
                HttpMethod.PUT,
                null,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, acceptResponse.getStatusCode());
    }

    @Test
    @DisplayName("Starts ride with valid input")
    public void startRide_withAllValidInput(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ResponseEntity<RideDTO> acceptResponse = acceptRide(createResponse);
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());

        ResponseEntity<RideDTO> startResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + createResponse.getBody().getId() + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());
        assertNotNull(startResponse.getBody());
        RideDTO ride = startResponse.getBody();
        assertEquals(createResponse.getBody().getId(), ride.getId());
        assertNull(ride.getRejection());
        assertNotNull(ride.getStartTime());
        assertNull(ride.getEndTime());
        assertEquals("STARTED", ride.getStatus().toString());

        ResponseEntity<RideDTO> endResponse =  endRide(startResponse);
        assertEquals(HttpStatus.OK, endResponse.getStatusCode());

    }
    @Test
    @DisplayName("Cant start ride that is not accepted")
    public void doesntStartRide_thatIsNotAccepted(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());


        ResponseEntity<ErrorResponseMessage> startResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + createResponse.getBody().getId() + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, startResponse.getStatusCode());
        assertEquals("Cannot start a ride that is not in status ACCEPTED!", startResponse.getBody().getMessage());

        // cleanup
        cancelRide(createResponse.getBody());

    }

    @Test
    @DisplayName("Cant start ride that is not created")
    public void doesntStartRide_thatIsNotCreated(){

        ResponseEntity<ErrorResponseMessage> startResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + 5 + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, startResponse.getStatusCode());
        assertEquals("Cannot start a ride that is not in status ACCEPTED!", startResponse.getBody().getMessage());

    }

    @Test
    @DisplayName("Cant start ride that is does not exist")
    public void doesntStartRide_thatDoesNotExist(){

        ResponseEntity<String> startResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + 1241 + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.NOT_FOUND, startResponse.getStatusCode());
        assertEquals("Ride does not exist!", startResponse.getBody());

    }
    @Test
    @DisplayName("Cant start ride as user different from DRIVER")
    public void doesntStartRide_asInvalidUser(){

        ResponseEntity<String> startResponse = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 1241 + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, startResponse.getStatusCode());
        assertEquals("Access Denied", startResponse.getBody());

    }

    @Test
    @DisplayName("Cant start ride as an unregistered user")
    public void doesntStartRide_asUnregisteredUser(){

        ResponseEntity<String> startResponse = this.restTemplate.exchange(
                BASE_PATH + "/" + 1241 + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.UNAUTHORIZED, startResponse.getStatusCode());
    }

    @Test
    @DisplayName("Cant start ride that has invalid id")
    public void doesntStartRide_thatHasInvalidId(){

        ResponseEntity<String> startResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + 0 + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, startResponse.getStatusCode());
    }

    @Test
    @DisplayName("Cant start ride that was withdrawn by passenger")
    public void doesntStartRide_thatIsWithdrawn(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        withdrawRide(createResponse.getBody());


        ResponseEntity<ErrorResponseMessage> startResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + createResponse.getBody().getId() + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, startResponse.getStatusCode());
        assertEquals("Cannot start a ride that is not in status ACCEPTED!", startResponse.getBody().getMessage());

    }
    @Test
    @DisplayName("Cant start ride that is cancelled by Driver")
    public void doesntStartRide_thatIsCancelled(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        cancelRide(createResponse.getBody());

        ResponseEntity<ErrorResponseMessage> startResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + createResponse.getBody().getId() + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, startResponse.getStatusCode());
        assertEquals("Cannot start a ride that is not in status ACCEPTED!", startResponse.getBody().getMessage());


    }


    @Test
    @DisplayName("Ends ride with valid input")
    public void endRide_withAllValidInput(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ResponseEntity<RideDTO> acceptResponse = acceptRide(createResponse);
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());

        ResponseEntity<RideDTO> startResponse = startRide(acceptResponse);
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());
        ResponseEntity<RideDTO> endResponse =  endRide(startResponse);
        assertEquals(HttpStatus.OK, endResponse.getStatusCode());
        assertNotNull(endResponse.getBody());
        RideDTO ride = endResponse.getBody();
        assertEquals(createResponse.getBody().getId(), ride.getId());
        assertNull(ride.getRejection());
        assertNotNull(ride.getStartTime());
        assertNotNull(ride.getEndTime());
        assertEquals("FINISHED", ride.getStatus().toString());

    }
    @Test
    @DisplayName("Doesnt end ride as a user that is not Driver")
    public void doesntEndRide_asInvalidUser(){

        ResponseEntity<String> endResponse = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 123 + "/end",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, endResponse.getStatusCode());
        assertEquals("Access Denied", endResponse.getBody());
    }

    @Test
    @DisplayName("Doesnt end ride as an unauthorized user")
    public void doesntEndRide_asUnauthorizedUser(){

        ResponseEntity<String> endResponse = this.restTemplate.exchange(
                BASE_PATH + "/" + 123 + "/end",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.UNAUTHORIZED, endResponse.getStatusCode());
    }

    @Test
    @DisplayName("Doesnt end ride that does not exist")
    public void doesntEndRide_thatDoesNotExist(){

        ResponseEntity<String> endResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + 124124 + "/end",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.NOT_FOUND, endResponse.getStatusCode());
        assertEquals("Ride does not exist!", endResponse.getBody());
    }

    @Test
    @DisplayName("Doesnt end ride that is not started")
    public void doesntEndRide_thatIsNotStarted(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ResponseEntity<RideDTO> acceptResponse = acceptRide(createResponse);
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());

        ResponseEntity<ErrorResponseMessage> endResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + acceptResponse.getBody().getId() + "/end",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, endResponse.getStatusCode());
        assertEquals("Cannot end a ride that is not in status STARTED!", endResponse.getBody().getMessage());

        // cleanup
        withdrawRide(acceptResponse.getBody());
    }

    @Test
    @DisplayName("Panic ride with valid input as a DRIVER")
    public void panicRide_withAllValidInputAsDriver(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ResponseEntity<RideDTO> acceptResponse = acceptRide(createResponse);
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());

        ResponseEntity<RideDTO> startResponse = startRide(acceptResponse);
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        ResponseEntity<RideDTO> panicResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + createResponse.getBody().getId() + "/panic",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO("Panic reason")),
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
        assertEquals(HttpStatus.OK, panicResponse.getStatusCode());
        assertEquals(RideStatus.FINISHED, panicResponse.getBody().getStatus());
        assertNotNull(panicResponse.getBody().getEndTime());

    }
    @Test
    @DisplayName("Panic ride with valid input as a PASSENGER")
    public void panicRide_withAllValidInputAsPassenger(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ResponseEntity<RideDTO> acceptResponse = acceptRide(createResponse);
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());

        ResponseEntity<RideDTO> startResponse = startRide(acceptResponse);
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        ResponseEntity<RideDTO> panicResponse = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + createResponse.getBody().getId() + "/panic",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO("Panic reason")),
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
        assertEquals(HttpStatus.OK, panicResponse.getStatusCode());
        assertEquals(RideStatus.FINISHED, panicResponse.getBody().getStatus());
        assertNotNull(panicResponse.getBody().getEndTime());

    }
    @Test
    @DisplayName("Doesnt Panic ride with non existing ride")
    public void doesntPanicRide_whenRideDoesntExist(){

        ResponseEntity<String> panicResponse = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 123534 + "/panic",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO("Panic reason")),
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.NOT_FOUND, panicResponse.getStatusCode());
        assertEquals("Ride does not exist!", panicResponse.getBody());
    }

    @Test
    @DisplayName("Doesnt Panic ride with no reason")
    public void doesntPanicRide_whenNoReasonGiver(){

        ResponseEntity<String> panicResponse = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 5 + "/panic",
                HttpMethod.PUT,
                new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, panicResponse.getStatusCode());
        assertEquals("Must give a reason!", panicResponse.getBody());
    }

    @Test
    @DisplayName("Doesnt Panic ride that doesnt have status STARTED")
    public void doesntPanicRide_whenRideIsNotStarted(){

        ResponseEntity<String> panicResponse = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 5 + "/panic",
                HttpMethod.PUT,
                new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, panicResponse.getStatusCode());
        assertEquals("Cannot panic a ride that is not started!", panicResponse.getBody());
    }



    private ResponseEntity<RideDTO> createRide() {
        LocationDTO departure = new LocationDTO("adresa", 19.55, 22.2);
        LocationDTO destination = new LocationDTO("adresa2", 19.22, 22.1);
        RouteDTO route = new RouteDTO(departure, destination);
        LinkedHashSet<RouteDTO> routes = new LinkedHashSet<>();
        routes.add(route);
        Set<UserDTO> users = new HashSet<>();
        CreateRideDTO createRide = new CreateRideDTO(users, routes, "STANDARD", false, false, null);

        return this.passengerRestTemplate.postForEntity(
                BASE_PATH,
                new HttpEntity<>(createRide),
                RideDTO.class
        );
    }


    private ResponseEntity<RideDTO> startRide(ResponseEntity<RideDTO> createResponse){
        return this.driverRestTemplate.exchange(
                BASE_PATH + "/" + createResponse.getBody().getId() + "/start",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
    }

    private ResponseEntity<RideDTO> acceptRide(ResponseEntity<RideDTO> createResponse) {
        return this.driverRestTemplate.exchange(
                BASE_PATH + "/" + createResponse.getBody().getId() + "/accept",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
    }
    private ResponseEntity<RideDTO> endRide(ResponseEntity<RideDTO> createResponse) {
        return this.driverRestTemplate.exchange(
                BASE_PATH + "/" + createResponse.getBody().getId() + "/end",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
    }

    private ResponseEntity<RideDTO> cancelRide(RideDTO ride) {
        return this.driverRestTemplate.exchange(
                BASE_PATH + "/" + ride.getId() + "/cancel",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO("I can't")),
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
    }
    private ResponseEntity<RideDTO> withdrawRide(RideDTO ride) {
        return this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + ride.getId() + "/withdraw",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO("I can't")),
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
    }
}
