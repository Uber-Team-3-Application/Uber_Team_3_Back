package com.reesen.Reesen.serivce;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.repository.*;
import com.reesen.Reesen.service.PassengerService;
import com.reesen.Reesen.service.RideService;
import com.reesen.Reesen.service.interfaces.ILocationService;
import com.reesen.Reesen.service.interfaces.IWorkingHoursService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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


    @Test
    @DisplayName(value = "Calculates the distance between two locations using the Haversine formula")
    public void calculateDistanceBetween_twoLocations(){
        Location departure = new Location( 14.44, 15.23, "adresa");
        Location destination = new Location( 15.44, 15.23, "adresa");


        double distance = this.rideService.calculateDistance(departure, destination);


        assertEquals( 111, distance, 0.2);

        departure.setLatitude(15.42);
        distance = this.rideService.calculateDistance(departure, destination);
        assertThat(distance).isLessThan(20);
    }

    @Test
    @DisplayName(value = "Calculate Distance - Returns 0 when any location is null")
    public void calculateDistance_returnsZero_forNullLocation(){
        Location departure = null;
        Location destination = new Location( 15.44, 15.23, "adresa");

        double distance = this.rideService.calculateDistance(departure, destination);

        assertEquals( 0, distance);

        destination = null;
        distance = this.rideService.calculateDistance(departure, destination);
        assertEquals(0, distance);
    }

    @Test
    @DisplayName(value = "Get all rides for a passenger with all valid data and no dates present")
    public void findAllRidesFor_passengerWithNoDates(){
        List<Ride> rideList = new ArrayList<>();
        rideList.add(new Ride());
        Page<Ride> pageRides = new PageImpl<>(rideList);
        Passenger passenger = new Passenger("Marko", "Preradovic", "profilna", "+38121521", "markopreradovic@gmail.com", "Marko123", null, null, true, 0);
        passenger.setId(4L);

        Mockito.when(this.passengerRepository.findById(4L)).thenReturn(Optional.of(passenger));
        Mockito.when(this.rideRepository.findAllRidesByPassengerId(4L, PageRequest.of(0, 10)))
                .thenReturn(pageRides);

        Page<Ride> rides = this.rideService.findAllRidesForPassenger(4L,
                                                                PageRequest.of(0, 10),
                                                                null, null);
        verify(this.rideRepository, times(1)).findAllRidesByPassengerId(4L, PageRequest.of(0, 10));
        assertNotNull(rides);
        assertThat(rides.getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName(value = "Get null instead of rides for a passenger with invalid data")
    public void findAllRidesFor_passenger_WithInvalidPassenger_WithDatesFromTo(){

        Mockito.when(passengerRepository.findById(12L)).thenReturn(Optional.empty());


        Page<Ride> rides = this.rideService.findAllRidesForPassenger(12L,
                                                        PageRequest.of(0, 10),
                                                        new Date(), new Date());
        verify(this.passengerRepository, times(1)).findById(12L);
        verify(this.rideRepository, times(0)).findAllRidesByPassengerId(4L, PageRequest.of(0, 10));
        assertNull(rides);

    }

    @Test
    @DisplayName(value = "Get all rides for a passenger with all valid data and end date null")
    public void findAllRidesFor_passengerWithStartDateAnd_EndDateNull(){
        List<Ride> rideList = new ArrayList<>();
        rideList.add(new Ride());
        Page<Ride> pageRides = new PageImpl<>(rideList);
        Passenger passenger = new Passenger("Marko", "Preradovic", "profilna", "+38121521", "markopreradovic@gmail.com", "Marko123", null, null, true, 0);
        passenger.setId(4L);
        Date now = new Date();
        Mockito.when(this.passengerRepository.findById(4L)).thenReturn(Optional.of(passenger));
        Mockito.when(this.rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfter(4L, now, PageRequest.of(0, 10)))
                .thenReturn(pageRides);

        Page<Ride> rides = this.rideService.findAllRidesForPassenger(4L,
                PageRequest.of(0, 10),
                now, null);


        verify(this.passengerRepository, times(1)).findById(4L);
        verify(this.rideRepository, times(1)).findAllRidesByPassengerIdAndTimeOfStartAfter(4L, now, PageRequest.of(0, 10));

        assertNotNull(rides);
        assertThat(rides.getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName(value = "Get all rides for a passenger with all valid data and end start date null")
    public void findAllRidesFor_passengerWithStartNullAnd_EndDateValid(){
        List<Ride> rideList = new ArrayList<>();
        rideList.add(new Ride());
        Page<Ride> pageRides = new PageImpl<>(rideList);
        Passenger passenger = new Passenger("Marko", "Preradovic", "profilna", "+38121521", "markopreradovic@gmail.com", "Marko123", null, null, true, 0);
        passenger.setId(4L);
        Date now = new Date();
        Mockito.when(this.passengerRepository.findById(4L)).thenReturn(Optional.of(passenger));
        Mockito.when(this.rideRepository.findAllRidesByPassengerIdAndTimeOfEndBefore(4L, now, PageRequest.of(0, 10)))
                .thenReturn(pageRides);

        Page<Ride> rides = this.rideService.findAllRidesForPassenger(4L,
                PageRequest.of(0, 10),
                null, now);


        verify(this.passengerRepository, times(1)).findById(4L);
        verify(this.rideRepository, times(1)).findAllRidesByPassengerIdAndTimeOfEndBefore(4L, now, PageRequest.of(0, 10));

        assertNotNull(rides);
        assertThat(rides.getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName(value = "Get all rides for a passenger with all valid data and start and end date present")
    public void findAllRidesFor_passengerWithStarValidAnd_EndDateValid(){
        List<Ride> rideList = new ArrayList<>();
        rideList.add(new Ride());
        Page<Ride> pageRides = new PageImpl<>(rideList);
        Passenger passenger = new Passenger("Marko", "Preradovic", "profilna", "+38121521", "markopreradovic@gmail.com", "Marko123", null, null, true, 0);
        passenger.setId(4L);
        Date now = new Date();
        Mockito.when(this.passengerRepository.findById(4L)).thenReturn(Optional.of(passenger));
        Mockito.when(this.rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(4L, now, now, PageRequest.of(0, 10)))
                .thenReturn(pageRides);

        Page<Ride> rides = this.rideService.findAllRidesForPassenger(4L,
                PageRequest.of(0, 10),
                now, now);


        verify(this.passengerRepository, times(1)).findById(4L);
        verify(this.rideRepository, times(1)).findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(4L, now, now, PageRequest.of(0, 10));

        assertNotNull(rides);
        assertThat(rides.getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName(value = "Get all rides for a user with the role NOT being DRIVER")
    public void findAllRidesFor_SpecificUser_WithRole(){
        Date now = new Date();
        setMocksForRidesWithSpecificRole(now);

        Page<Ride> rides = rideService.findAllForUserWithRole(4L,
                PageRequest.of(0, 10),
                now, now, Role.PASSENGER);

        verify(rideRepository, times(1)).findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(4L, now, now, PageRequest.of(0, 10));

        assertNotNull(rides);
        assertThat(rides.getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName(value = "Get all rides for a user with the role DRIVER")
    public void findAllRidesFor_SpecificUser_WithRoleDriver(){
        Date now = new Date();
        setMocksForRidesWithDriverRole(now);

        Page<Ride> rides = rideService.findAllForUserWithRole(4L,
                PageRequest.of(0, 10),
                now, now, Role.DRIVER);

        verify(rideRepository, times(1)).findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(4L, now, now, PageRequest.of(0, 10));

        assertNotNull(rides);
        assertThat(rides.getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName(value = "Finds all locations by VALID Ride id")
    public void findAllLocationsBy_rideId(){
        Long validId = 1L;
        Long invalidId = 12412421L;
        setMocksForFindLocationsByRideId(validId, invalidId);

        Set<Route> routes = this.rideService.getLocationsByRide(validId);

        assertNotNull(routes);
        assertEquals(1, routes.size());

    }

    @Test
    @DisplayName(value = "Finds all locations by INVALID Ride id")
    public void findAllLocationsBy_invalidRideId(){
        Long validId = 1L;
        Long invalidId = 12412421L;
        setMocksForFindLocationsByRideId(validId, invalidId);

        Set<Route> routes = this.rideService.getLocationsByRide(invalidId);

        assertNull(routes);

    }

    @Test
    @DisplayName(value = "Finds Passenger Active Ride with VALID id")
    public void findPassengerActiveRideBy_passengerId(){
        Ride ride = new Ride();
        ride.setId(10L);
        Long passengerId = 1L;
        setMocksForPassengerActiveRide(ride, passengerId);

        Ride passengerActiveRide = this.rideService.findPassengerActiveRide(passengerId);

        assertNotNull(passengerActiveRide);
        assertEquals(ride.getId(), passengerActiveRide.getId());
    }

    @Test
    @DisplayName(value = "Finds Passenger Active Ride with INVALID id")
    public void findPassengerActiveRideBy_invalidPassengerId(){

        Long passengerId = 1L;
        Mockito.when(this.rideRepository.findPassengerActiveRide(passengerId, RideStatus.STARTED))
                .thenReturn(null);

        Ride passengerActiveRide = this.rideService.findPassengerActiveRide(passengerId);

        assertNull(passengerActiveRide);
    }


    @Test
    @DisplayName(value = "")
    public void getFilteredRideWith_validInput(){

    }



    private void setMocksForPassengerActiveRide(Ride ride, Long passengerId) {
        Mockito.when(this.rideRepository.findPassengerActiveRide(passengerId, RideStatus.STARTED))
                .thenReturn(ride);
        Mockito.when(this.rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
        Mockito.when(this.driverRepository.findDriverByRidesContaining(ride))
                .thenReturn(Optional.of(new Driver()));
        Mockito.when(this.rideRepository.findDriverByRideId(ride.getId()))
                .thenReturn(new Driver());
        Location departure = new Location( 14.44, 15.23, "adresa");
        Location destination = new Location( 15.44, 15.23, "adresa");
        Route route = new Route(departure, destination);
        route.setId(1L);
        LinkedHashSet<Route> locations = new LinkedHashSet<>();
        locations.add(route);
        Mockito.when(this.rideRepository.getLocationsByRide(ride.getId()))
                .thenReturn(locations);
        Mockito.when(this.routeRepository.getDepartureByRoute(route))
                .thenReturn(Optional.of(departure));
        Mockito.when(this.routeRepository.getDestinationByRoute(route))
                .thenReturn(Optional.of(destination));
    }

    private void setMocksForFindLocationsByRideId(Long validId, Long invalidId){
        Location departure = new Location( 14.44, 15.23, "adresa");
        Location destination = new Location( 15.44, 15.23, "adresa");
        Route route = new Route(departure, destination);
        route.setId(1L);
        LinkedHashSet<Route> locations = new LinkedHashSet<>();
        locations.add(route);

        Mockito.when(this.rideRepository.getLocationsByRide(validId)).thenReturn(locations);
        Mockito.when(this.rideRepository.getLocationsByRide(invalidId)).thenReturn(null);
    }

    private void setMocksForRidesWithDriverRole(Date now){
        List<Ride> rideList = new ArrayList<>();
        rideList.add(new Ride());
        Page<Ride> pageRides = new PageImpl<>(rideList);

        Mockito.when(rideRepository.findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(4L, now, now, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(this.rideRepository.findAllByDriverId(4L, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(this.rideRepository.findAllByDriverIdAndTimeOfStartAfter(4L, now, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(this.rideRepository.findAllByDriverIdAndTimeOfEndBefore(4L, now, PageRequest.of(0, 10)))
                .thenReturn(pageRides);

    }
    private void setMocksForRidesWithSpecificRole(Date now){
        List<Ride> rideList = new ArrayList<>();
        rideList.add(new Ride());
        Page<Ride> pageRides = new PageImpl<>(rideList);

        Mockito.when(passengerRepository.findById(4L))
                .thenReturn(Optional.of(new Passenger()));
        Mockito.when(rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(4L, now, now, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(this.rideRepository.findAllRidesByPassengerId(4L, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(this.rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfter(4L, now, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(this.rideRepository.findAllRidesByPassengerIdAndTimeOfEndBefore(4L, now, PageRequest.of(0, 10)))
                .thenReturn(pageRides);

    }



}
