package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.ErrorResponseMessage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        ResponseEntity<RideDTO> withdraw = cancelRide(acceptResponse.getBody());
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

        ResponseEntity<RideDTO> acceptRide = acceptRide(createResponse);
        assertEquals(HttpStatus.OK, acceptRide.getStatusCode());

        ResponseEntity<RideDTO> withdrawResponse = withdrawRide(acceptRide.getBody());


        ResponseEntity<ErrorResponseMessage> startResponse = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + withdrawResponse.getBody().getId() + "/start",
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

        ResponseEntity<ErrorResponseMessage> panicResponse = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 5 + "/panic",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO("He is crazy")),
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, panicResponse.getStatusCode());
        assertEquals("Cannot panic a ride that is not started!", panicResponse.getBody().getMessage());
    }

    @Test
    @DisplayName("Doesnt Panic ride as an invalid User")
    public void doesntPanicRide_whenUserIsInvalid(){
        headers.set("X-Auth-Token", null);
        HttpEntity<LoginDTO> adminLogin = new HttpEntity<>(new LoginDTO("nikolaj@gmail.com", "Nikolaj123"), headers);
        ResponseEntity<TokenDTO> adminResponse = restTemplate
                .exchange("/api/user/login",
                        HttpMethod.POST,
                        adminLogin,
                        new ParameterizedTypeReference<TokenDTO>() {
                        });

        String adminToken = adminResponse.getBody().getToken();
        headers.set("X-Auth-Token", adminToken);
        ResponseEntity<String> panicResponse = this.restTemplate.exchange(
                BASE_PATH + "/" + 5 + "/panic",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO("He is crazy"), headers),
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, panicResponse.getStatusCode());
        assertEquals("Access Denied", panicResponse.getBody());
    }
    @Test
    @DisplayName("Doesnt Panic ride as an unauthenticated User")
    public void doesntPanicRide_whenUserIsNotAuthenticated(){
        ResponseEntity<String> panicResponse = this.restTemplate.exchange(
                BASE_PATH + "/" + 5 + "/panic",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.UNAUTHORIZED, panicResponse.getStatusCode());
    }

    // CREATE A RIDE

    @Test
    public void createARide(){
        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        RideDTO ride = createResponse.getBody();
        assertNotNull(ride.getDriver());
        assertEquals(1, ride.getPassengers().size());
        assertNull(ride.getScheduledTime());
        assertEquals("PENDING", ride.getStatus().toString());

        ResponseEntity<RideDTO> withdraw = cancelRide(ride);
        assertEquals(HttpStatus.OK, withdraw.getStatusCode());
    }

    @Test
    public void createARide_InvalidInput(){
        ResponseEntity<String> response = this.passengerRestTemplate.exchange(
                BASE_PATH,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createARide_AlreadyPending(){
        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        ResponseEntity<ErrorResponseMessage> response = this.passengerRestTemplate.exchange(
                BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(createResponse.getBody()),
                new ParameterizedTypeReference<ErrorResponseMessage>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot create a ride while you have one already pending!", response.getBody().getMessage());
        ResponseEntity<RideDTO> withdraw = cancelRide(createResponse.getBody());
        assertEquals(HttpStatus.OK, withdraw.getStatusCode());
    }

    @Test
    public void createARide_asInvalidUser(){

        ResponseEntity<String> response = this.driverRestTemplate.exchange(
                BASE_PATH,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access Denied", response.getBody());
    }

    @Test
    public void createARide_withNoTokenPresent(){

        ResponseEntity<String> response = this.restTemplate.exchange(
                BASE_PATH,
                HttpMethod.POST,
                null,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // --------------------------------------------------------------------

    // GET DRIVER ACTIVE RIDE
    @Test
    public void get_DriverActiveRide(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        ResponseEntity<RideDTO> acceptResponse = acceptRide(createResponse);
        assertNotNull(acceptResponse.getBody());
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());

        ResponseEntity<RideDTO> startedRide = startRide(acceptResponse);
        assertNotNull(startedRide.getBody());
        assertEquals(HttpStatus.OK, startedRide.getStatusCode());

        ResponseEntity<RideDTO> response = getDriverActiveRide(startedRide.getBody().getDriver().getId());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RideDTO ride = response.getBody();
        assertNotNull(ride.getDriver());
        assertEquals(1, ride.getPassengers().size());
        assertNull(ride.getScheduledTime());
        assertEquals("STARTED", ride.getStatus().toString());

        ResponseEntity<RideDTO> withdraw = cancelRide(ride);
        assertEquals(HttpStatus.OK, withdraw.getStatusCode());
    }


    @Test
    public void getDriverActiveRide_nonExistingRide(){
        ResponseEntity<String> response = this.driverRestTemplate.exchange(
                BASE_PATH + "/driver/" + 2 + "/active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals("Active ride does not exist", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getDriverActiveRide_BadRequest_invalidId(){

        ResponseEntity<String> response = this.driverRestTemplate.exchange(
                BASE_PATH + "/driver/" + 21598+ "/active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void getDriverActiveRide_asInvalidUser(){

        ResponseEntity<String> response = this.passengerRestTemplate.exchange(
                BASE_PATH + "/driver/" + 2 + "/active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access Denied", response.getBody());
    }

    @Test
    public void getDriverActiveRide_withNoTokenPresent(){

        ResponseEntity<String> response = this.restTemplate.exchange(
                BASE_PATH + "/driver/" + 2 + "/active",
                HttpMethod.GET,
                null,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    // --------------------------------------------------------------------

    // GET PASSENGER ACTIVE RIDE
    @Test
    public void get_PassengerActiveRide(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        ResponseEntity<RideDTO> acceptResponse = acceptRide(createResponse);
        assertNotNull(acceptResponse.getBody());
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());

        ResponseEntity<RideDTO> startedRide = startRide(acceptResponse);
        assertNotNull(startedRide.getBody());
        assertEquals(HttpStatus.OK, startedRide.getStatusCode());

        ResponseEntity<RideDTO> response = getPassengerActiveRide(passengerId);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RideDTO ride = response.getBody();
        assertNotNull(ride.getDriver());
        assertEquals(1, ride.getPassengers().size());
        assertNull(ride.getScheduledTime());
        assertEquals("STARTED", ride.getStatus().toString());

        ResponseEntity<RideDTO> withdraw = cancelRide(ride);
        assertEquals(HttpStatus.OK, withdraw.getStatusCode());
    }


    @Test
    public void getPassengerActiveRide_nonExistingRide(){
        ResponseEntity<String> response = this.passengerRestTemplate.exchange(
                BASE_PATH + "/passenger/" + 4 + "/active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals("Active ride does not exist", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getPassengerActiveRide_BadRequest_invalidId(){

        ResponseEntity<String> response = this.passengerRestTemplate.exchange(
                BASE_PATH + "/passenger/" + 21598+ "/active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void getPassengerActiveRide_asInvalidUser(){

        ResponseEntity<String> response = this.driverRestTemplate.exchange(
                BASE_PATH + "/passenger/" + 4 + "/active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access Denied", response.getBody());
    }

    @Test
    public void getPassengerActiveRide_withNoTokenPresent(){

        ResponseEntity<String> response = this.restTemplate.exchange(
                BASE_PATH + "/passenger/" + 4 + "/active",
                HttpMethod.GET,
                null,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    // --------------------------------------------------------------------

    // GET RIDE
    @Test
    public void getRide_Success(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        ResponseEntity<RideDTO> response = getRideDetails(createResponse.getBody().getId());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        RideDTO ride = response.getBody();
        assertNotNull(ride.getDriver());
        assertEquals(1, ride.getPassengers().size());
        assertNotNull(ride.getStatus().toString());

        ResponseEntity<RideDTO> withdraw = cancelRide(ride);
        assertEquals(HttpStatus.OK, withdraw.getStatusCode());
    }


    @Test
    public void getRide_nonExistingRide(){
        ResponseEntity<String> response = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 23456,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals("Ride does not exist", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getRide_BadRequest_invalidId(){

        ResponseEntity<String> response = this.passengerRestTemplate.exchange(
                BASE_PATH + "/passenger/" + -21598+ "/active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void getRide_withNoTokenPresent(){

        ResponseEntity<String> response = this.restTemplate.exchange(
                BASE_PATH + "/" + 4,
                HttpMethod.GET,
                null,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


    // --------------------------------------------------------------------

    // WITHDRAW RIDE
    @Test
    public void withdraw_acceptedRide_withActiveDriver(){

        ResponseEntity<RideDTO> createResponse = createRide();
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        ResponseEntity<RideDTO> acceptResponse = acceptRide(createResponse);
        assertNotNull(acceptResponse.getBody());
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());

        ResponseEntity<RideDTO> withdrawResponse = withdrawRide(acceptResponse.getBody());
        assertNotNull(withdrawResponse.getBody());
        assertEquals(HttpStatus.OK, withdrawResponse.getStatusCode());
        RideDTO ride = withdrawResponse.getBody();
        assertNotNull(ride.getDriver());
        assertEquals(1, ride.getPassengers().size());
        assertNull(ride.getScheduledTime());
        assertEquals("CANCELED", ride.getStatus().toString());

    }

    @Test
    public void withdraw_nonExistingRide(){
        ResponseEntity<String> response = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 1234 + "/withdraw",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals("Ride does not exist!", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void doesntWithdrawRide_BadRequest_invalidId(){

        ResponseEntity<String> response = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 0 + "/withdraw",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void doesntWithdrawRide_WithStatus_DifferentThatStartedOrAccepted() {

        ResponseEntity<String> response = this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + 5 + "/withdraw",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ride not started or accepted!", response.getBody());
    }

    /** @author Veljko */

    @Test
    @DisplayName("Should cancel ride when inputs are valid")
    public void cancelRide_withAllValidInputs() {
        ResponseEntity<RideDTO> createdRide = createRide();
        String reason = "I can't to drive";
        RideDTO rideDTO = createdRide.getBody();
        headers.set("X-Auth-Token", driverToken);
        assert rideDTO != null;
        ResponseEntity<RideDTO> dto  = this.restTemplate.exchange(
                BASE_PATH + "/" + rideDTO.getId() + "/cancel",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO(reason), headers),
                new ParameterizedTypeReference<RideDTO>() {
                }
        );

        assertEquals(HttpStatus.OK, dto.getStatusCode());
        assertEquals(RideStatus.CANCELED, Objects.requireNonNull(dto.getBody()).getStatus());
        assertEquals(reason, dto.getBody().getRejection().getReason());

    }

    /** @author Veljko */

    @ParameterizedTest
    @ValueSource(longs = {0L, -10L})
    @DisplayName("Doesn't cancel ride when ride ID is invalid")
    public void cancelRide_whenRideIdIsInvalid(Long rideId) {
        String reason = "I can't to drive";
        headers.set("X-Auth-Token", driverToken);
        ResponseEntity<RideDTO> dto  = this.restTemplate.exchange(
                BASE_PATH + "/" + rideId + "/cancel",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO(reason), headers),
                new ParameterizedTypeReference<RideDTO>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, dto.getStatusCode());

    }

    /** @author Veljko */

    @ParameterizedTest
    @ValueSource(longs = {100L, 256L})
    @DisplayName("Doesn't cancel ride when ride doesn't exists")
    public void cancelRide_whenRideNotExist(Long rideId) {
        String reason = "I can't to drive";
        headers.set("X-Auth-Token", driverToken);
        ResponseEntity<String> dto  = this.restTemplate.exchange(
                BASE_PATH + "/" + rideId + "/cancel",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO(reason), headers),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.NOT_FOUND, dto.getStatusCode());
        assertEquals("Ride does not exist!", dto.getBody());
    }

    /** @author Veljko */

    @Test
    @DisplayName("Doesn't cancel ride when reason is null")
    public void cancelRide_whenReasonIsNull() {
        ResponseEntity<RideDTO> createdRide = createRide();
        RideDTO rideDTO = createdRide.getBody();
        headers.set("X-Auth-Token", driverToken);
        assert rideDTO != null;
        ResponseEntity<String> dto  = this.restTemplate.exchange(
                BASE_PATH + "/" + rideDTO.getId() + "/cancel",
                HttpMethod.PUT,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, dto.getStatusCode());
        assertEquals("Must give a reason!", dto.getBody());

        cancelRide(createdRide.getBody());

    }

    /** @author Veljko */

    @Test
    @DisplayName("Doesn't cancel ride as an invalid User ")
    public void cancelRide_whenUserIsInvalid() {
        ResponseEntity<RideDTO> createdRide = createRide();
        RideDTO rideDTO = createdRide.getBody();
        headers.set("X-Auth-Token",this.passengerToken);
        assert rideDTO != null;
        ResponseEntity<String> response  = this.restTemplate.exchange(
                BASE_PATH + "/" + rideDTO.getId() + "/cancel",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO("I can't"), headers),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access Denied", response.getBody());

        cancelRide(createdRide.getBody());

    }

    /** @author Veljko */

    @Test
    @DisplayName("Doesn't cancel ride as an Unauthenticated User ")
    public void cancelRide_whenUserIsUnauthenticated() {
        ResponseEntity<String> response  = this.restTemplate.exchange(
                BASE_PATH + "/" + 5 + "/cancel",
                HttpMethod.PUT,
                new HttpEntity<>(new ReasonDTO("I can't")),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }


    /** @author Veljko */
    @Test
    @DisplayName("Create favorite locations for quick selection with valid inputs")
    public void createFavouriteLocations_forQuickSelections_whenInputIsValid() {
        ResponseEntity<RideDTO> createdRide = createRide();
        RideDTO ride = createdRide.getBody();
        String favouriteName = "To mom's house";

        CreateFavoriteRideDTO favoriteRideDTO = new CreateFavoriteRideDTO(favouriteName, ride.getPassengers(),
                ride.getLocations(), ride.getVehicleType().name(), ride.isBabyTransport(), ride.isPetTransport());

        headers.set("X-Auth-Token", this.passengerToken);
        ResponseEntity<FavoriteRideDTO> response  = this.restTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.POST,
                new HttpEntity<>(favoriteRideDTO, headers),
                new ParameterizedTypeReference<>() {
                }
        );
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        cancelRide(createdRide.getBody());
        deleteFavoriteRide(response.getBody());

    }
    /** @author Veljko */
    @Test
    @DisplayName("Should not favorite locations for quick selection when input is Null")
    public void createFavouriteLocations_forQuickSelections_whenInputIsNull() {
        ResponseEntity<RideDTO> createdRide = createRide();
        RideDTO ride = createdRide.getBody();


        headers.set("X-Auth-Token", this.passengerToken);
        ResponseEntity<?> response  = this.restTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.POST,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        cancelRide(createdRide.getBody());

    }

    /** @author Veljko */
    @Test
    @DisplayName("Should not favorite locations for quick selection with invalid input")
    public void createFavouriteLocations_forQuickSelections_withInvalidInput() {
        ResponseEntity<RideDTO> createdRide = createRide();
        RideDTO ride = createdRide.getBody();
        String favouriteName = "To mom's house";

        System.out.println(ride.toString());
        CreateFavoriteRideDTO favoriteRideDTO = new CreateFavoriteRideDTO(favouriteName, null,
                null, ride.getVehicleType().name(), ride.isBabyTransport(), ride.isPetTransport());

        headers.set("X-Auth-Token", this.passengerToken);
        ResponseEntity<?> response  = this.restTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.POST,
                new HttpEntity<>(favoriteRideDTO, headers),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        cancelRide(createdRide.getBody());

    }

    /** @author Veljko */
    @Test
    @DisplayName("Should not add to favorite locations when ride already exists ")
    public void createFavouriteLocations_forQuickSelections_whenRideAlreadyExists() {

        ResponseEntity<RideDTO> createdRide = createRide();
        RideDTO ride = createdRide.getBody();
        assert ride != null;
        ResponseEntity<FavoriteRideDTO> createdFavorite = createFavoriteRide(ride);

        String favouriteName = "To mom's house";

        CreateFavoriteRideDTO favoriteRideDTO = new CreateFavoriteRideDTO(favouriteName, ride.getPassengers(),
                ride.getLocations(), ride.getVehicleType().name(), ride.isBabyTransport(), ride.isPetTransport());

        ResponseEntity<String> responseEntity = this.passengerRestTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.POST,
                new HttpEntity<>(favoriteRideDTO),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("You already have a ride with this name!", responseEntity.getBody());
        cancelRide(createdRide.getBody());
        deleteFavoriteRide(createdFavorite.getBody());

    }

    /** @author Veljko */
    @Test
    @DisplayName("Should not add to favorite locations as an invalid User ")
    public void createFavouriteLocations_forQuickSelections_whenUserIsInvalid() {
        ResponseEntity<RideDTO> createdRide = createRide();
        RideDTO ride = createdRide.getBody();
        String favouriteName = "To mom's house";

        CreateFavoriteRideDTO favoriteRideDTO = new CreateFavoriteRideDTO(favouriteName, ride.getPassengers(),
                ride.getLocations(), ride.getVehicleType().name(), ride.isBabyTransport(), ride.isPetTransport());
        headers.set("X-Auth-Token", this.driverToken);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.POST,
                new HttpEntity<>(favoriteRideDTO, headers),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Access Denied", responseEntity.getBody());
        cancelRide(createdRide.getBody());

    }


    /** @author Veljko */
    @Test
    @DisplayName("Should not add to favorite locations as Unauthenticated User ")
    public void createFavouriteLocations_forQuickSelections_whenUserIsUnauthenticated() {

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.POST,
                new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }


    /** @author Veljko */
    @Test
    @DisplayName("Should get favourite locations ")
    public void getFavouriteLocations() {

        ResponseEntity<RideDTO> createdRide = createRide();
        RideDTO ride = createdRide.getBody();
        assert ride != null;
        createFavoriteRide(ride);

        ResponseEntity<Set<FavoriteRideDTO>> responseEntity = this.passengerRestTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        Set<FavoriteRideDTO> favoriteRides = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, favoriteRides.size());
        cancelRide(createdRide.getBody());
        FavoriteRideDTO[] favoriteRideDTOS = responseEntity.getBody().toArray(new FavoriteRideDTO[0]);
        deleteFavoriteRide(favoriteRideDTOS[0]);


    }


    /** @author Veljko */
    @Test
    @DisplayName("Should not get favourite locations when user is ")
    public void getFavouriteLocations_whenUserIsUnauthenticated() {

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    /** @author Veljko */
    @Test
    @DisplayName("Should not get favourite locations as an invalid User  ")
    public void getFavouriteLocations_asAnInvalidUser() {

        ResponseEntity<String> responseEntity = this.driverRestTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Access Denied", responseEntity.getBody());

    }

    /** @author Veljko*/
    @Test
    @DisplayName("Should delete favorite ride")
    public void deleteFavoriteRide_whenInputsAreValid() {
        ResponseEntity<RideDTO> createdRide = createRide();
        RideDTO ride = createdRide.getBody();
        ResponseEntity<FavoriteRideDTO> favourite = createFavoriteRide(ride);
        FavoriteRideDTO favoriteRide = favourite.getBody();
        ResponseEntity<String> responseEntity = this.passengerRestTemplate.exchange(
                BASE_PATH + "/favorites/" + favoriteRide.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        cancelRide(createdRide.getBody());

    }


    /** @author Veljko*/
    @Test
    @DisplayName("Should not delete favorite ride when doesn't exists")
    public void deleteFavoriteRide_whenFavoriteRideNotExist() {

        ResponseEntity<String> responseEntity = this.passengerRestTemplate.exchange(
                BASE_PATH + "/favorites/" + 5,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    /** @author Veljko*/
    @ParameterizedTest
    @ValueSource(longs = {-50L, 0L})
    @DisplayName("Should not delete favorite ride id is not valid")
    public void deleteFavoriteRide_whenFavoriteRideIdIsLessThanOne(Long id) {

        ResponseEntity<String> responseEntity = this.passengerRestTemplate.exchange(
                BASE_PATH + "/favorites/" + id,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /** @author Veljko*/
    @Test
    @DisplayName("Should not delete favorite ride when user is unauthorized")
    public void deleteFavoriteRide_whenUserIsUnauthorized() {
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(
                BASE_PATH + "/favorites/" + 5,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    /** @author Veljko*/
    @Test
    @DisplayName("Should not delete favorite ride as Invalid user")
    public void deleteFavoriteRide_whenUserIsInvalid() {
        ResponseEntity<String> responseEntity = this.driverRestTemplate.exchange(
                BASE_PATH + "/favorites/" + 5,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Access Denied", responseEntity.getBody());

    }

    @Test
    public void doesntWithdrawRide_asInvalidUser(){

        ResponseEntity<String> response = this.driverRestTemplate.exchange(
                BASE_PATH + "/" + 5 + "/withdraw",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access Denied", response.getBody());
    }

    @Test
    public void doesntWithdrawRide_withNoTokenPresent(){

        ResponseEntity<String> response = this.restTemplate.exchange(
                BASE_PATH + "/5/withdraw",
                HttpMethod.PUT,
                null,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // --------------------------------------------------------------------

    private ResponseEntity<RideDTO> createRide() {
        LocationDTO departure = new LocationDTO("address first", 19.55, 22.2);
        LocationDTO destination = new LocationDTO("address second", 19.22, 22.1);
        RouteDTO route = new RouteDTO(departure, destination);
        LinkedHashSet<RouteDTO> routes = new LinkedHashSet<>();
        routes.add(route);
        Set<UserDTO> users = new HashSet<>();
        UserDTO user = new UserDTO(4L, "mika@gmail.com");
        users.add(user);
        CreateRideDTO createRide = new CreateRideDTO(users, routes, "STANDARD", false, false, null);

        return this.passengerRestTemplate.postForEntity(
                BASE_PATH,
                new HttpEntity<>(createRide),
                RideDTO.class
        );
    }

    private ResponseEntity<FavoriteRideDTO> createFavoriteRide(RideDTO ride) {


        CreateFavoriteRideDTO favoriteRideDTO = new CreateFavoriteRideDTO("To mom's house", ride.getPassengers(),
                ride.getLocations(), ride.getVehicleType().name(), ride.isBabyTransport(), ride.isPetTransport());

        headers.set("X-Auth-Token", this.passengerToken);
        return this.restTemplate.exchange(
                BASE_PATH + "/favorites",
                HttpMethod.POST,
                new HttpEntity<>(favoriteRideDTO, headers),
                new ParameterizedTypeReference<>() {
                }
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
    private ResponseEntity<String> deleteFavoriteRide(FavoriteRideDTO rideDTO) {
        return this.passengerRestTemplate.exchange(
                BASE_PATH + "/favorites/" + rideDTO.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    private ResponseEntity<RideDTO> withdrawRide(RideDTO ride) {
        return this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + ride.getId() + "/withdraw",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
    }

    private ResponseEntity<RideDTO> getDriverActiveRide(Long id) {
        return this.driverRestTemplate.exchange(
                BASE_PATH + "/driver/" + id + "/active",
                HttpMethod.GET,
               null,
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
    }

    private ResponseEntity<RideDTO> getPassengerActiveRide(Long id) {
        return this.passengerRestTemplate.exchange(
                BASE_PATH + "/passenger/" + id + "/active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
    }

    private ResponseEntity<RideDTO> getRideDetails(Long id) {
        return this.passengerRestTemplate.exchange(
                BASE_PATH + "/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RideDTO>() {
                }
        );
    }

}
