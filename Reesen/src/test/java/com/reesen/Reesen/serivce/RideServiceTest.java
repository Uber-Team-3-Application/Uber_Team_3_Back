package com.reesen.Reesen.serivce;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.*;
import static org.mockito.Mockito.*;

import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.repository.*;
import com.reesen.Reesen.service.PassengerService;
import com.reesen.Reesen.service.RideService;
import com.reesen.Reesen.service.interfaces.ILocationService;
import com.reesen.Reesen.service.interfaces.IWorkingHoursService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
public class RideServiceTest {

    @Autowired
    private RideService rideService;

    @MockBean
    private RideRepository rideRepository;

    @MockBean
    private RouteRepository routeRepository;

    @MockBean
    private PassengerRepository passengerRepository;


    @MockBean
    private VehicleRepository vehicleTypeRepository;

    @MockBean
    private PanicRepository panicRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DriverRepository driverRepository;

    @MockBean
    private IWorkingHoursService workingHoursService;

    @MockBean
    private ILocationService locationService;

    @MockBean
    private DeductionRepository deductionRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private PassengerService passengerService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    // VALIDATE CREATE A RIDE DTO
    @Mock
    private CreateRideDTO createRideDTO;

    @Test
    public void validateCreateRideDTOPassNullPassengers() {
        when(createRideDTO.getPassengers()).thenReturn(null);
        when(createRideDTO.getLocations()).thenReturn(createLocations());
        when(createRideDTO.getVehicleType()).thenReturn("Car");
        assertTrue(this.rideService.validateCreateRideDTO(createRideDTO));
    }

    @Test
    public void validateCreateRideDTOPassNullLocations() {
        when(createRideDTO.getPassengers()).thenReturn(createPassengers());
        when(createRideDTO.getLocations()).thenReturn(null);
        when(createRideDTO.getVehicleType()).thenReturn("Car");
        assertTrue(this.rideService.validateCreateRideDTO(createRideDTO));
    }

    @Test
    public void validateCreateRideDTOPassNullVehicleType() {
        when(createRideDTO.getPassengers()).thenReturn(createPassengers());
        when(createRideDTO.getLocations()).thenReturn(createLocations());
        when(createRideDTO.getVehicleType()).thenReturn(null);
        assertTrue(this.rideService.validateCreateRideDTO(createRideDTO));
    }

    @Test
    public void validateCreateRideDTOPassNullEmail() {
        when(createRideDTO.getPassengers()).thenReturn(createPassengersWithNullEmail());
        when(createRideDTO.getLocations()).thenReturn(createLocations());
        when(createRideDTO.getVehicleType()).thenReturn("Car");
        assertTrue(this.rideService.validateCreateRideDTO(createRideDTO));
    }

    @Test
    public void validateCreateRideDTOPassNullDeparture() {
        when(createRideDTO.getPassengers()).thenReturn(createPassengers());
        when(createRideDTO.getLocations()).thenReturn(createLocationsWithNullDeparture());
        when(createRideDTO.getVehicleType()).thenReturn("Car");
        assertTrue(this.rideService.validateCreateRideDTO(createRideDTO));
    }

    @Test
    public void validateCreateRideDTOPassNullDestination() {
        when(createRideDTO.getPassengers()).thenReturn(createPassengers());
        when(createRideDTO.getLocations()).thenReturn(createLocationsWithNullDestination());
        when(createRideDTO.getVehicleType()).thenReturn("Car");
        assertTrue(this.rideService.validateCreateRideDTO(createRideDTO));
    }

    private Set<UserDTO> createPassengers() {
        Set<UserDTO> passengers = new HashSet<>();
        UserDTO passenger = new UserDTO();
        passenger.setEmail("test@example.com");
        passengers.add(passenger);
        return passengers;
    }

    private Set<UserDTO> createPassengersWithNullEmail() {
        Set<UserDTO> passengers = new HashSet<>();
        UserDTO passenger = new UserDTO();
        passenger.setEmail(null);
        passengers.add(passenger);
        return passengers;
    }

    private LinkedHashSet<RouteDTO> createLocations() {
        LinkedHashSet<RouteDTO> locations = new LinkedHashSet<>();
        RouteDTO location = new RouteDTO();
        location.setDeparture(new LocationDTO("A", 1, 1));
        location.setDestination(new LocationDTO("B", 1, 1));
        locations.add(location);
        return locations;
    }

    private LinkedHashSet<RouteDTO> createLocationsWithNullDeparture() {
        LinkedHashSet<RouteDTO> locations = new LinkedHashSet<>();
        RouteDTO location = new RouteDTO();
        location.setDeparture(null);
        location.setDestination(new LocationDTO("B", 1, 1));
        locations.add(location);
        return locations;
    }

    private LinkedHashSet<RouteDTO> createLocationsWithNullDestination() {
        LinkedHashSet<RouteDTO> locations = new LinkedHashSet<>();
        RouteDTO location = new RouteDTO();
        location.setDeparture(new LocationDTO("A", 1, 1));
        location.setDestination(null);
        locations.add(location);
        return locations;
    }

    // CHECK FOR PENDING RIDE
    @Test
    public void testCheckForPendingRide_WithPendingRides() {
        Long passengerId = 123L;
        Set<Ride> rides = Collections.singleton(new Ride());
        when(rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.PENDING)).thenReturn(rides);
        boolean result = this.rideService.checkForPendingRide(passengerId);
        assertTrue(result);
    }

    @Test
    public void testCheckForPendingRide_WithAcceptedRides() {
        Long passengerId = 123L;
        Set<Ride> rides = Collections.singleton(new Ride());
        when(rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.ACCEPTED)).thenReturn(rides);
        boolean result = this.rideService.checkForPendingRide(passengerId);
        assertTrue(result);
    }

    @Test
    public void testCheckForPendingRide_WithStartedRides() {
        Long passengerId = 123L;
        Set<Ride> rides = Collections.singleton(new Ride());
        when(rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.STARTED)).thenReturn(rides);
        boolean result = this.rideService.checkForPendingRide(passengerId);
        assertTrue(result);
    }

    @Test
    public void testCheckForPendingRide_WithActiveRides() {
        Long passengerId = 123L;
        Set<Ride> rides = Collections.singleton(new Ride());
        when(rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.ACTIVE)).thenReturn(rides);
        boolean result = this.rideService.checkForPendingRide(passengerId);
        assertTrue(result);
    }

    @Test
    public void testCheckForPendingRide_WithoutPendingRides() {
        Long passengerId = 123L;
        when(rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.PENDING)).thenReturn(Collections.emptySet());
        when(rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.ACCEPTED)).thenReturn(Collections.emptySet());
        when(rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.STARTED)).thenReturn(Collections.emptySet());
        when(rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.ACTIVE)).thenReturn(Collections.emptySet());
        boolean result = this.rideService.checkForPendingRide(passengerId);
        assertFalse(result);
    }

    // START RIDE
    @Test
    public void startRide_WhenRideIsNotFound_ShouldReturnNull() {
        Long id = 1L;
        Optional<Ride> emptyOptional = Optional.empty();
        when(rideRepository.findById(id)).thenReturn(emptyOptional);
        RideDTO result = rideService.startRide(id);
        assertNull(result);
    }

    public Ride setUpFindOne(Long id){
        Ride ride = new Ride();
        Driver driver = new Driver();
        driver.setId(987L);
        ride.setDriver(driver);
        ride.setVehicleType(new VehicleType(0, VehicleName.STANDARD));
        ride.setId(id);
        Optional<Ride> optionalRide = Optional.of(ride);
        when(rideRepository.findById(id)).thenReturn(optionalRide);
        when(rideRepository.findById(id)).thenReturn(Optional.of(ride));
        when(driverRepository.findDriverByRidesContaining(ride)).thenReturn(Optional.of(driver));
        when(rideRepository.findDriverByRideId(id)).thenReturn(driver);
        when(rideRepository.findPassengerByRideId(id)).thenReturn(new HashSet<>());
        when(rideRepository.getLocationsByRide(id)).thenReturn(new LinkedHashSet<>());
        return ride;
    }

    @Test
    public void startRide_WhenRideIsFound_ShouldSetStatusAndTimeOfStart() {
        Long id = 1L;
        setUpFindOne(id);
        RideDTO rideDTO = rideService.startRide(id);
        assertNotNull(rideDTO);
        assertEquals(RideStatus.STARTED, rideDTO.getStatus());
        assertNotNull(rideDTO.getStartTime());
        Date now = new Date();
        assertTrue(rideDTO.getStartTime().compareTo(now) <= 0);
    }

    @Test
    public void startRide_ShouldSaveTheUpdatedRide() {
        Long id = 1L;
        Ride ride = setUpFindOne(id);
        rideService.startRide(id);
        verify(rideRepository, times(1)).save(ride);
    }

    @Test
    public void startRide_ShouldNotifyPassengersAndDriverThroughWebSockets() {
        Long id = 1L;
        Ride ride = setUpFindOne(id);
        try {
            rideService.startRide(id);
            for (Passenger p : ride.getPassengers()) {
                verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/topic/passenger/start-ride/" + p.getId()), any(RideDTO.class));
            }
            verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/topic/driver/start-ride/" + ride.getDriver().getId()), any(RideDTO.class));
        } catch (Exception e) {
            System.err.println("Error sending WebSocket message: " + e.getMessage());
        }
    }

    @Test
    public void startRide_ShouldReturnCorrectRideDTO() {
        Long id = 1L;
        Ride ride = setUpFindOne(id);
        RideDTO result = rideService.startRide(id);
        assertEquals(new RideDTO(ride).getId(), result.getId());
        assertEquals(new RideDTO(ride).getVehicleType(), result.getVehicleType());
        assertEquals(new RideDTO(ride).getDriver().getId(), result.getDriver().getId());
    }

    @Test
    public void testStartRideHandlesExceptions() throws Exception {
        Long id = 1L;
        Ride ride = setUpFindOne(id);
        doThrow(new RuntimeException()).when(simpMessagingTemplate).convertAndSend(anyString(), (Object) any());
        RideDTO rideDTO = rideService.startRide(id);
        assertEquals(new RideDTO(ride), rideDTO);
        verify(rideRepository, times(1)).save(ride);
        for (Passenger p : ride.getPassengers()) {
            verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/topic/passenger/start-ride/" + p.getId()), eq(new RideDTO(ride)));
        }
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/topic/driver/start-ride/" + ride.getDriver().getId()), eq(new RideDTO(ride)));
    }

    @Test
    public void testStartRideWithInvalidId() {
        Long id = -1L;
        RideDTO rideDTO = rideService.startRide(id);
        assertNull(rideDTO);
        id = null;
        rideDTO = rideService.startRide(id);
        assertNull(rideDTO);
        verify(rideRepository, times(0)).save(any());
        verify(simpMessagingTemplate, times(0)).convertAndSend(anyString(), (Object) any());
    }

    // FIND ONE


    // CREATE RIDE


    // FIND DRIVER ACTIVE RIDE


    // WITHDRAW RIDE


    // GET ALL ACTIVE RIDES


    // PASSENGER REPORTS
}

