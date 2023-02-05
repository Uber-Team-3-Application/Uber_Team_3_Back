package com.reesen.Reesen.serivce;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.Enums.TypeOfReport;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.*;
import static org.mockito.Mockito.*;

import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.paginated.Paginated;
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
    private VehicleTypeRepository vehicleTypeRepository;

    @MockBean
    private VehicleRepository vehicleRepository;

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
        Driver driver = new Driver("Mika", "Janic", "", "8563", "mika@gmail.com", "123", new HashSet<Document>(), new HashSet<Ride>(), new Vehicle());
        driver.setId(6L);
        Passenger passenger = new Passenger("Jana", "Janic", "", "8563", "jana@gmail.com", "123", new HashSet<Ride>(), new HashSet<FavoriteRide>(), true, 0);
        Set<Passenger> passengers = new HashSet<>();
        passenger.setId(5L);
        passengers.add(passenger);
        LinkedHashSet<Route> routes = new LinkedHashSet<>();
        Location departure = new Location( 1, 1, "A");
        Location destination = new Location(2, 2, "B");
        routes.add(new Route(departure, destination));
        Ride ride = new Ride();
        ride.setPassengers(passengers);
        ride.setDriver(driver);
        ride.setVehicleType(new VehicleType(0, VehicleName.STANDARD));
        ride.setId(id);
        ride.setLocations(routes);

        when(rideRepository.findById(id)).thenReturn(Optional.of(ride));
        when(driverRepository.findDriverByRidesContaining(ride)).thenReturn(Optional.of(driver));
        when(rideRepository.findDriverByRideId(id)).thenReturn(driver);
        when(rideRepository.findPassengerByRideId(id)).thenReturn(passengers);
        when(rideRepository.getLocationsByRide(id)).thenReturn(routes);
        when(routeRepository.getDepartureByRoute(any(Route.class))).thenReturn(Optional.of(departure));
        when(routeRepository.getDestinationByRoute(any(Route.class))).thenReturn(Optional.of(destination));
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

    @Test
    public void testFindOneValidIdAndRideExists() {
        Long id = 1L;

        Ride ride = this.setUpFindOne(id);

        Ride returnedRide = rideService.findOne(id);

        assertNotNull(returnedRide);
        assertEquals(ride, returnedRide);
        assertEquals(ride.getDriver(), returnedRide.getDriver());
        assertEquals(ride.getPassengers(), returnedRide.getPassengers());
        assertEquals(ride.getLocations(), returnedRide.getLocations());
    }

    @Test
    public void testFindOneInvalidId() {
        Long id = 1L;
        this.setUpFindOne(5L);
        Ride returnedRide = rideService.findOne(id);

        assertNull(returnedRide);
    }

    @Test
    public void testFindOneNullId() {
        this.setUpFindOne(1L);
        Ride returnedRide = rideService.findOne(null);

        assertNull(returnedRide);
    }

    @Test
    public void testFindOneValidIdAndRideExistsNoDriver() {
        Long id = 1L;

        Ride ride = this.setUpFindOne(id);
        when(rideRepository.findDriverByRideId(id)).thenReturn(null);
        Ride returnedRide = rideService.findOne(id);

        assertNotNull(returnedRide);
        assertNull(returnedRide.getDriver());
        assertEquals(ride.getPassengers(), returnedRide.getPassengers());
        assertEquals(ride.getLocations(), returnedRide.getLocations());
    }

    @Test
    public void testFindOneValidIdAndScheduledTimeAndRideExists() {
        Long id = 1L;

        Ride ride = this.setUpFindOne(id);
        ride.setScheduledTime(new Date());
        when(rideRepository.findById(id)).thenReturn(Optional.of(ride));
        Ride returnedRide = rideService.findOne(id);

        assertNotNull(returnedRide);
        assertEquals(ride, returnedRide);
        assertEquals(ride.getDriver(), returnedRide.getDriver());
        assertEquals(ride.getPassengers(), returnedRide.getPassengers());
        assertEquals(ride.getLocations(), returnedRide.getLocations());
        assertEquals(ride.getScheduledTime(), returnedRide.getScheduledTime());
    }


    // FIND DRIVER ACTIVE RIDE

    @Test
    public void testFindDriverActiveRideValidIdAndRideExists() {
        Long id = 1L;
        Ride ride = this.setUpFindOne(id);
        Long driverId = ride.getDriver().getId();
        ride.setStatus(RideStatus.STARTED);
        when(rideRepository.findById(id)).thenReturn(Optional.of(ride));
        when(rideRepository.findRideByDriverIdAndStatus(driverId, RideStatus.STARTED)).thenReturn(Optional.of(ride));

        Ride returnedRide = this.rideService.findDriverActiveRide(driverId);

        assertNotNull(returnedRide);
        assertEquals(ride, returnedRide);
        assertEquals(ride.getDriver().getId(), returnedRide.getDriver().getId());
        assertEquals(RideStatus.STARTED, returnedRide.getStatus());
    }

    @Test
    public void testFindDriverActiveRideInvalidId() {
        Long id = 1L;
        Ride ride = this.setUpFindOne(id);
        ride.setStatus(RideStatus.STARTED);
        when(rideRepository.findById(id)).thenReturn(Optional.of(ride));
        when(rideRepository.findRideByDriverIdAndStatus(ride.getDriver().getId(), RideStatus.STARTED)).thenReturn(Optional.of(ride));
        Long driverId = 1L;
        Ride returnedRide = this.rideService.findDriverActiveRide(driverId);

        assertNull(returnedRide);
    }

    @Test
    public void testFindDriverActiveRideNullId() {
        Long id = 1L;
        Ride ride = this.setUpFindOne(id);
        ride.setStatus(RideStatus.STARTED);
        when(rideRepository.findById(id)).thenReturn(Optional.of(ride));
        when(rideRepository.findRideByDriverIdAndStatus(ride.getDriver().getId(), RideStatus.STARTED)).thenReturn(Optional.of(ride));
        Ride returnedRide = this.rideService.findDriverActiveRide(null);

        assertNull(returnedRide);
    }

    @Test
    public void testFindDriverActiveRideValidIdAndStatusNotStarted() {
        Long id = 1L;
        Ride ride = this.setUpFindOne(id);
        Long driverId = ride.getDriver().getId();
        when(rideRepository.findRideByDriverIdAndStatus(driverId, ride.getStatus())).thenReturn(Optional.empty());

        Ride returnedRide = this.rideService.findDriverActiveRide(driverId);

        assertNull(returnedRide);
    }


    // WITHDRAW RIDE
    @Test
    public void testWithdrawRideValidIdAndStatusCanceled() {
        Long id = 1L;
        this.setUpFindOne(id);
        RideDTO returnedRide = this.rideService.withdrawRide(id);

        assertNotNull(returnedRide);
        assertEquals(RideStatus.CANCELED, returnedRide.getStatus());
    }

    @Test
    public void testWithdrawRideInvalidId() {
        Long id = 1L;
        this.setUpFindOne(id);
        RideDTO returnedRide = this.rideService.withdrawRide(2l);

        assertNull(returnedRide);
    }

    @Test
    public void testWithdrawRideNullId() {
        Long id = 1L;
        this.setUpFindOne(id);
        RideDTO returnedRide = this.rideService.withdrawRide(null);

        assertNull(returnedRide);
    }

    // ALL ACTIVE RIDES

    @Test
    public void getAllActiveRides_returnsAllActiveRides() {
        List<RideWithVehicleDTO> activeRides = new ArrayList<>();
        activeRides.add(new RideWithVehicleDTO(1L, 1L, 30.0, 30.0, "Address 1"));
        activeRides.add(new RideWithVehicleDTO(2L, 2L, 40.0, 40.0, "Address 2"));
        activeRides.add(new RideWithVehicleDTO(3L, 3L, 50.0, 50.0, "Address 3"));
        when(rideRepository.getAllActiveRides(RideStatus.ACTIVE, RideStatus.STARTED)).thenReturn(activeRides);
        List<RideWithVehicleDTO> result = rideService.getALlActiveRides();
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getRideId());
        assertEquals(2L, result.get(1).getRideId());
        assertEquals(3L, result.get(2).getRideId());
    }

    @Test
    public void whenGetAllActiveRidesIsCalledAndNoActiveRides_thenReturnEmptyList() {
        when(rideRepository.getAllActiveRides(RideStatus.ACTIVE, RideStatus.STARTED))
                .thenReturn(new ArrayList<>());
        List<RideWithVehicleDTO> result = this.rideService.getALlActiveRides();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // CREATE RIDE
    private CreateRideDTO setUpCreateRide(){
        LocationDTO departure = new LocationDTO( "A",1, 1);
        LocationDTO destination = new LocationDTO("B", 2, 2);
        LinkedHashSet<RouteDTO> routes = new LinkedHashSet<>();
        routes.add(new RouteDTO(departure, destination));
        Passenger passenger = new Passenger("Jana", "Janic", "", "8563", "jana@gmail.com", "123", new HashSet<Ride>(), new HashSet<FavoriteRide>(), true, 0);
        passenger.setId(5L);
        Driver driver = new Driver("Mika", "Janic", "", "8563", "mika@gmail.com", "123", new HashSet<Document>(), new HashSet<Ride>(), new Vehicle());
        driver.setId(6L);
        CreateRideDTO createRideDTO = new CreateRideDTO(new HashSet<>(), routes, "Standard", false, false, null);
        when(this.passengerRepository.findById(5L)).thenReturn(Optional.of(passenger));
        when(this.vehicleTypeRepository.findByName(any())).thenReturn(new VehicleType(100, VehicleName.STANDARD));
        when(this.passengerRepository.getPassengerRides(passenger.getId())).thenReturn(new HashSet<>());
        when(this.driverRepository.getDriverRides(any())).thenReturn(new HashSet<>());
        ArrayList<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        when(this.driverRepository.findAllByIsActive(true)).thenReturn(drivers);
        Vehicle vehicle = new Vehicle(driver, "dasda", "123", 4, true, true, new Location(3, 3, "c"), new VehicleType(100, VehicleName.STANDARD));
        vehicle.setPassengerSeats(3);
        when(this.driverRepository.getVehicle(driver.getId())).thenReturn(vehicle);
        when(this.routeRepository.save(any())).thenReturn(new Route(new Location(1, 1, "A"), new Location(2, 2, "B")));
        Ride newRide = new Ride();
        newRide.setId(1L);
        when(this.rideRepository.save(any())).thenReturn(newRide);
        when(this.locationService.getFirstLocation(any())).thenReturn(new Location(1, 1, "A"));
        when(this.locationService.getLastLocation(any())).thenReturn(new Location(2, 2, "B"));
        return createRideDTO;
    }
    @Test
    public void testCreateRideDTOValidInputScheduledTimeNull() {
        CreateRideDTO createRideDTO = setUpCreateRide();
        RideDTO rideDTO = this.rideService.createRideDTO(createRideDTO, 5L);
        assertNotNull(rideDTO.getDriver());
        assertEquals(createRideDTO.getVehicleType().toLowerCase(), rideDTO.getVehicleType().name().toLowerCase());
        assertNotEquals(0, rideDTO.getEstimatedTimeInMinutes());
        assertNotEquals(0, rideDTO.getTotalCost());
        assertEquals(RideStatus.PENDING, rideDTO.getStatus());
        assertNotNull(rideDTO.getPassengers());
        assertNotNull(rideDTO.getLocations());
    }

    @Test
    public void testCreateRideDTOValidInputScheduledTimeNullNoDriver() {
        CreateRideDTO createRideDTO = setUpCreateRide();
        when(this.driverRepository.findAllByIsActive(true)).thenReturn(new ArrayList<>());
        RideDTO rideDTO = this.rideService.createRideDTO(createRideDTO, 5L);
        assertNull(rideDTO);
    }

    @Test
    public void testCreateRideDTOValidInputScheduledTimeNotNull() {
        CreateRideDTO createRideDTO = setUpCreateRide();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 20);
        createRideDTO.setScheduledTime(calendar.getTime());
        RideDTO rideDTO = this.rideService.createRideDTO(createRideDTO, 5L);
        assertNull(rideDTO.getDriver());
        assertEquals(RideStatus.SCHEDULED, rideDTO.getStatus());
        assertNotNull(rideDTO.getPassengers());
        assertNotNull(rideDTO.getLocations());
    }

    @Test
    public void testCreateRideDTOValidInputScheduledTimeMoreTheFiveHours() {
        CreateRideDTO createRideDTO = setUpCreateRide();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 6);
        createRideDTO.setScheduledTime(calendar.getTime());
        RideDTO rideDTO = this.rideService.createRideDTO(createRideDTO, 5L);
        assertNull(rideDTO);
    }

    @Test
    public void testCreateRideDTOInvalidId() {
        CreateRideDTO createRideDTO = setUpCreateRide();
        RideDTO rideDTO = this.rideService.createRideDTO(createRideDTO, 5757L);
        assertNull(rideDTO);
    }

}

