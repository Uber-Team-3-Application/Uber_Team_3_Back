package com.reesen.Reesen.serivce;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.repository.*;
import com.reesen.Reesen.service.PassengerService;
import com.reesen.Reesen.service.RideService;
import com.reesen.Reesen.service.interfaces.ILocationService;
import com.reesen.Reesen.service.interfaces.IWorkingHoursService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @DisplayName(value = "Get all rides for a user with the role PASSENGER")
    public void findAllRidesFor_SpecificUser_WithRole(){
        RideService spyRideService = spy(rideService);

        List<Ride> rideList = new ArrayList<>();
        rideList.add(new Ride());
        Page<Ride> pageRides = new PageImpl<>(rideList);

        Date now = new Date();
        Mockito.when(passengerRepository.findById(4L))
                .thenReturn(Optional.of(new Passenger()));
        Mockito.when(rideRepository.findAllRidesByPassengerId(4L, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(spyRideService.findAllRidesForPassenger(4L, PageRequest.of(0, 10), now, now))
                .thenReturn(pageRides);


        Page<Ride> rides = spyRideService.findAllForUserWithRole(4L,
                PageRequest.of(0, 10),
                now, now, Role.PASSENGER);

        verify(spyRideService, times(1)).findAllRidesForPassenger(4L, PageRequest.of(0, 10), now, now);

        assertNotNull(rides);
        assertThat(rides.getNumberOfElements()).isGreaterThan(0);
    }


}
