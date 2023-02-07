package com.reesen.Reesen.serivce;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.Enums.TypeOfReport;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.repository.*;
import com.reesen.Reesen.service.PassengerService;
import com.reesen.Reesen.service.RideService;
import com.reesen.Reesen.service.interfaces.ILocationService;
import com.reesen.Reesen.service.interfaces.IWorkingHoursService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.TestPropertySource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

        verify(this.rideRepository, times(1)).findPassengerActiveRide(passengerId, RideStatus.STARTED);

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
        verify(this.rideRepository, times(1)).findPassengerActiveRide(passengerId, RideStatus.STARTED);

        assertNull(passengerActiveRide);
    }


    @Test
    @DisplayName(value = "Gets filtered Ride with valid inputs and no deduction")
    public void getFilteredRideWith_validInput(){

        Ride ride = new Ride();
        ride.setId(1L);
        ride.setVehicleType(new VehicleType(123, VehicleName.STANDARD));
        setMocksForFilteredRide(ride, ride.getId());
        PassengerRideDTO passengerRideDTO = this.rideService.getFilteredRide(ride, 1L);


        verify(this.passengerRepository, times(1)).findPassengersByRidesContaining(ride);
        verify(this.deductionRepository, times(1)).findDeductionByRide(ride);

        assertNotNull(passengerRideDTO);
        assertNull(passengerRideDTO.getRejection());
        assertEquals(1L, passengerRideDTO.getDriver().getId());
        assertEquals("STANDARD", passengerRideDTO.getVehicleType());

    }

    @Test
    @DisplayName(value = "Gets filtered Ride with valid inputs and Deduction")
    public void getFilteredRideWith_validInputAnd_Deduction(){

        Ride ride = new Ride();
        ride.setId(0L);
        ride.setVehicleType(new VehicleType(123, VehicleName.STANDARD));
        setMocksForFilteredRide(ride, 1L);


        PassengerRideDTO passengerRideDTO = this.rideService.getFilteredRide(ride, 1L);


        verify(this.passengerRepository, times(1)).findPassengersByRidesContaining(ride);
        verify(this.deductionRepository, times(1)).findDeductionByRide(ride);

        assertNotNull(passengerRideDTO);
        assertNotNull(passengerRideDTO.getRejection());
        assertEquals(1L, passengerRideDTO.getDriver().getId());
        assertEquals("STANDARD", passengerRideDTO.getVehicleType());

    }
    @Test
    @DisplayName(value = "Gets filtered Ride with valid inputs and no driver")
    public void getFilteredRideWith_validInputAnd_noDriver(){

        Ride ride = new Ride();
        ride.setId(1L);
        ride.setVehicleType(new VehicleType(123, VehicleName.STANDARD));
        setMocksForFilteredRide(ride, ride.getId());


        PassengerRideDTO passengerRideDTO = this.rideService.getFilteredRide(ride, 0L);


        verify(this.passengerRepository, times(1)).findPassengersByRidesContaining(ride);
        verify(this.deductionRepository, times(1)).findDeductionByRide(ride);

        assertNotNull(passengerRideDTO);
        assertEquals(0L, passengerRideDTO.getDriver().getId());
        assertEquals("STANDARD", passengerRideDTO.getVehicleType());

    }

    @Test
    @DisplayName(value = "Gets filtered Rides with Driver Id Being One")
    public void getFilteredRidesWithDriverIdValid(){

        Ride first = new Ride();
        first.setId(1L);
        first.setVehicleType(new VehicleType(123, VehicleName.STANDARD));

        Ride second = new Ride();
        second.setId(2L);
        second.setVehicleType(new VehicleType(200, VehicleName.LUXURY));
        List<Ride> rides = new ArrayList<>();
        rides.add(first);
        rides.add(second);
        Page<Ride> pageRides = new PageImpl<>(rides);

        for(Ride ride: pageRides)
            setMocksForFilteredRide(ride, ride.getId());

        Set<PassengerRideDTO> passengerRideDTO = this.rideService.getFilteredRides(pageRides, 1L);


        assertNotNull(passengerRideDTO);
        assertEquals(2, passengerRideDTO.size());
        for(PassengerRideDTO ride: passengerRideDTO){
            assertEquals(1L, ride.getDriver().getId());
            if(ride.getId() == 1L) assertEquals("STANDARD", ride.getVehicleType());
            else assertEquals("LUXURY", ride.getVehicleType());
        }

    }


    @Test
    @DisplayName(value = "Gets filtered Rides with Driver Id Being Zero")
    public void getFilteredRidesWithDriverIdInvalid(){

        Ride first = new Ride();
        first.setId(1L);
        first.setVehicleType(new VehicleType(123, VehicleName.STANDARD));

        Ride second = new Ride();
        second.setId(2L);
        second.setVehicleType(new VehicleType(200, VehicleName.LUXURY));
        List<Ride> rides = new ArrayList<>();
        rides.add(first);
        rides.add(second);
        Page<Ride> pageRides = new PageImpl<>(rides);

        for(Ride ride: pageRides)
            setMocksForFilteredRide(ride, ride.getId());

        Set<PassengerRideDTO> passengerRideDTO = this.rideService.getFilteredRides(pageRides, 0L);


        assertNotNull(passengerRideDTO);
        assertEquals(2, passengerRideDTO.size());
        for(PassengerRideDTO ride: passengerRideDTO){
            assertEquals(0L, ride.getDriver().getId());
            if(ride.getId() == 1L) assertEquals("STANDARD", ride.getVehicleType());
            else assertEquals("LUXURY", ride.getVehicleType());
        }

    }


    @Captor
    private ArgumentCaptor<Panic> panicArgumentCaptor;

    @Test
    @DisplayName("Should  set panic when inputs are valid")
    public void setPanicForRide_whenRideIdIsValid_andPassengerIdIsValid() {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setStatus(RideStatus.ACTIVE);
        Driver driver = new Driver();
        driver.setId(2L);
        driver.setActive(true);
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("luke@gmail.com");
        setMocksForPanicRide(ride, 123L, 15L, passenger, driver);
        RideDTO dto = this.rideService.panicRide(ride.getId(), "Something wrong", passenger.getId());

        assertNotNull(dto);
        assertEquals(dto.getDriver().getId(), driver.getId());
        assertThat(dto.getPassengers().size()).isEqualTo(1);

        verify(panicRepository, times(1)).save(panicArgumentCaptor.capture());
        assertThat(panicArgumentCaptor.getValue().getRide().getId()).isEqualTo(ride.getId());
        assertThat(panicArgumentCaptor.getValue().getReason()).isEqualTo("Something wrong");
        assertThat(panicArgumentCaptor.getValue().getUser().getId()).isEqualTo(1L);


    }


    @ParameterizedTest
    @ValueSource(longs = {123L, -5L, 3L})
    @DisplayName("Should not set panic when ride ID is invalid")
    public void setPanicForRide_whenRideIdIsInvalid_andPassengerIdIsValid(Long invalidId) {
        Ride ride = new Ride();
        ride.setId(invalidId);
        ride.setStatus(RideStatus.ACTIVE);
        Driver driver = new Driver();
        driver.setId(2L);
        driver.setActive(true);
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("luke@gmail.com");
        setMocksForPanicRide(ride, invalidId, 15L, passenger, driver);
        RideDTO dto = this.rideService.panicRide(ride.getId(), "Something wrong", passenger.getId());

        assertNull(dto);
        verifyNoInteractions(panicRepository);


    }

    @ParameterizedTest
    @ValueSource(longs = {15L, 25L, 156L})
    @DisplayName("Should not set panic when passenger ID is invalid")
    public void setPanicForRide_whenRideIdIsValid_andPassengerIdIsInvalid(Long passengerId) {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setStatus(RideStatus.ACTIVE);
        Driver driver = new Driver();
        driver.setId(2L);
        driver.setActive(true);
        Passenger passenger = new Passenger();
        passenger.setId(passengerId);
        passenger.setEmail("luke@gmail.com");
        setMocksForPanicRide(ride, 123L, passengerId, passenger, driver);
        RideDTO dto = this.rideService.panicRide(ride.getId(), "Something wrong", passenger.getId());
        assertNull(dto);
        verifyNoInteractions(panicRepository);

    }

    @Captor
    private ArgumentCaptor<RideStatus> rideStatusArgumentCaptor;
    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;
    @Captor
    private ArgumentCaptor<Ride> rideArgumentCaptor;

    /** @author Veljko */

    @Test
    @DisplayName("Should cancel ride when ride ID is valid")
    public void cancelRide_withValidRideId() {

        String reason = "It's too late";
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setStatus(RideStatus.PENDING);
        setMocksForCancelAndEndRide(ride, 123L);

        Deduction deduction = new Deduction(ride, ride.getDriver(), reason, LocalDateTime.now());
        Mockito.when(deductionRepository.save(any())).thenReturn(deduction);

        this.rideService.cancelRide(ride.getId(), reason);

        verify(rideRepository, times(1)).updateRideStatus(longArgumentCaptor.capture(),
                rideStatusArgumentCaptor.capture());

        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());

        assertThat(rideArgumentCaptor.getValue().getId()).isEqualTo(1L);
        assertThat(rideStatusArgumentCaptor.getValue()).isEqualTo(RideStatus.CANCELED);
        assertThat(longArgumentCaptor.getValue()).isEqualTo(1L);
    }

    /** @author Veljko */

    @ParameterizedTest
    @ValueSource(longs = {123L, -50L, 0L})
    @DisplayName("Should not cancel ride when ride ID is invalid")
    public void cancelRide_withInvalidRideId(Long rideId) {

        String reason = "It's too late";
        Ride ride = new Ride();
        ride.setId(rideId);
        ride.setStatus(RideStatus.PENDING);
        setMocksForCancelAndEndRide(ride, rideId);

        Deduction deduction = new Deduction(ride, ride.getDriver(), reason, LocalDateTime.now());
        Mockito.when(deductionRepository.save(any())).thenReturn(deduction);

        RideDTO dto = this.rideService.cancelRide(ride.getId(), reason);

        assertNull(dto);
        verify(rideRepository, times(1)).findById(rideId);
    }


    @Test
    @DisplayName("Should end ride when ride ID is valid")
    public void endRide_withValidRideId() {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setStatus(RideStatus.ACTIVE);
        setMocksForCancelAndEndRide(ride, 123L);
        this.rideService.endRide(1L);

        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
        assertThat(rideArgumentCaptor.getValue().getId()).isEqualTo(1L);
        assertThat(rideArgumentCaptor.getValue().getStatus()).isEqualTo(RideStatus.FINISHED);


    }

    @ParameterizedTest
    @ValueSource(longs = {123L, -50L, 0L})
    @DisplayName("Should not end ride when ride ID is invalid")
    public void endRide_withInvalidRideId(Long rideId) {

        Ride ride = new Ride();
        ride.setId(rideId);
        ride.setStatus(RideStatus.ACTIVE);
        setMocksForCancelAndEndRide(ride, rideId);
        RideDTO dto = this.rideService.endRide(rideId);

        assertNull(dto);
        verify(rideRepository, times(0)).save(rideArgumentCaptor.capture());

    }

    @Test
    @DisplayName("Should accept ride when ride ID is valid")
    public void acceptRide_whenRideIdIsValid() {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setStatus(RideStatus.PENDING);
        setMocksForCancelAndEndRide(ride, 123L);
        RideDTO dto = this.rideService.acceptRide(1L);

        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
        assertThat(rideArgumentCaptor.getValue().getId()).isEqualTo(1L);
        assertThat(rideArgumentCaptor.getValue().getStatus()).isEqualTo(RideStatus.ACCEPTED);
        assertThat(dto.getId()).isEqualTo(1L);

    }


    @ParameterizedTest
    @ValueSource(longs = {123L, -50L, 0L})
    @DisplayName("Should not accept ride when ride ID is invalid")
    public void acceptRide_withInvalidRideId(Long rideId) {

        Ride ride = new Ride();
        ride.setId(rideId);
        ride.setStatus(RideStatus.PENDING);
        setMocksForCancelAndEndRide(ride, rideId);
        RideDTO dto = this.rideService.acceptRide(rideId);
        verify(rideRepository, times(1)).findById(rideId);
        assertNull(dto);

    }


    @Test
    @DisplayName("Find all rides without pageable and valid inputs")
    public void findAllWithoutPageable() {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setStatus(RideStatus.PENDING);
        setMocksForCancelAndEndRide(ride, 1L);

        Set<Ride> listOfRides  = new HashSet<>();
        listOfRides.add(ride);
        Mockito.when(rideRepository.findAll()).thenReturn(listOfRides.stream().toList());

        List<Ride> rides = this.rideService.findAll();

        verify(rideRepository, times(1)).findAll();
        assertThat(rides.size()).isEqualTo(1);
        assertThat(rides.get(0).getId()).isEqualTo(1L);

    }


    @Test
    @DisplayName("Find all rides with pageable and valid inputs")
    public void findAllWithPageable_andValidDriverId_andValidDatesFromTo() {
        Date now = new Date();
        setMocksForRidesWithDriverRole(now);
        Page<Ride> rides =  rideService.findAll(4L, PageRequest.of(0, 10), now, now);

        verify(rideRepository, times(1)).findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(4L, now, now, PageRequest.of(0, 10));
        assertNotNull(rides);
        assertThat(rides).isNotEmpty();

    }



    @ParameterizedTest
    @ValueSource(longs = {50L, 20L, -4L})
    @DisplayName("Find all rides with pageable, valid dates FROM,TO and invalid ID of driver")
    public void findAllWithPageable_andInvalidDriverId_andValidDatesFromTo(Long driverId) {
        Date now = new Date();
        setMocksForRidesWithDriverRole(now);
        Page<Ride> rides =  rideService.findAll(driverId, PageRequest.of(0, 10), now, now);

        verify(rideRepository, times(1)).findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(driverId, now, now, PageRequest.of(0, 10));

        assertNull(rides);
    }


    @Test
    @DisplayName("Find all rides with pageable when dates are null")
    public void findAllWithPageable_andValidDriverId_andNullableDates() {
        setMocksForRidesWithDriverRole(null);
        Page<Ride> rides =  rideService.findAll(4L, PageRequest.of(0, 10), null, null);

        verify(rideRepository, times(1)).findAllByDriverId(4L, PageRequest.of(0, 10));
        assertNotNull(rides);
        assertThat(rides).isNotEmpty();

    }



    @Test
    @DisplayName("Find all rides with pageable when FROM date is null")
    public void findAllWithPageable_andValidDriverId_whenFromIsNull() {
        Date to = new Date();
        setMocksForGettingRides(null, to);
        Page<Ride> rides =  rideService.findAll(4L, PageRequest.of(0, 10), null, to);

        verify(rideRepository, times(1)).findAllByDriverIdAndTimeOfEndBefore(4L,to, PageRequest.of(0, 10));
        assertNotNull(rides);
        assertThat(rides).isNotEmpty();

    }



    @Test
    @DisplayName("Find all rides with pageable when to date is null")
    public void findAllWithPageable_andValidDriverId_whenToIsNull() {
        Date from = new Date();
        setMocksForGettingRides(from, null);
        Page<Ride> rides =  rideService.findAll(4L, PageRequest.of(0, 10), from, null);

        verify(rideRepository, times(1)).findAllByDriverIdAndTimeOfStartAfter(4L,from, PageRequest.of(0, 10));
        assertNotNull(rides);
        assertThat(rides).isNotEmpty();

    }


    @Test
    @DisplayName(value = "Gets Driver Reports for Rides Per Day")
    public void getsReportsForDriver_RidesPerDay(){
        Date now = new Date();
        Date later = Date.from(Instant.now().plus(2, ChronoUnit.DAYS));

        ReportRequestDTO reportRequestDTO = new ReportRequestDTO(1L,Role.DRIVER, TypeOfReport.RIDES_PER_DAY, now, later);
        List<ReportDTO<Long>> reports = new ArrayList<>();
        reports.add(new ReportDTO<>(now, 255L));
        reports.add(new ReportDTO<>(later, 300L));

        Mockito.when(this.rideRepository.getRidesPerDayForSpecificDriver(reportRequestDTO.getFrom(), reportRequestDTO.getTo(), 1L))
                .thenReturn(reports);

        ReportSumAverageDTO report = this.rideService.getReportForDriver(reportRequestDTO);

        verify(this.rideRepository, times(1)).
                getRidesPerDayForSpecificDriver(reportRequestDTO.getFrom(), reportRequestDTO.getTo(), 1L);

        assertEquals(2, report.getResult().size());
        assertEquals(555, report.getSum());
        assertEquals(277.5, report.getAverage(), 0.1);
    }

    @Test
    @DisplayName(value = "Gets Reports for specific driver Money Earned Per Day")
    public void getsReport_MoneyEarnedPerDay(){

        Date now = new Date();
        Date later = Date.from(Instant.now().plus(2, ChronoUnit.DAYS));

        ReportRequestDTO reportRequestDTO = new ReportRequestDTO("DRIVER", "MONEY_EARNED_PER_DAY", now, later, 1L);
        List<ReportDTO<Double>> reports = new ArrayList<>();
        reports.add(new ReportDTO<>(now, 255.0));
        reports.add(new ReportDTO<>(later, 300.0));

        Mockito.when(this.rideRepository.getTotalCostPerDayForDriver(reportRequestDTO.getFrom(), reportRequestDTO.getTo(), 1L))
                .thenReturn(reports);

        ReportSumAverageDTO report = this.rideService.getReportForDriver(reportRequestDTO);

        verify(this.rideRepository, times(1)).
                getTotalCostPerDayForDriver(reportRequestDTO.getFrom(), reportRequestDTO.getTo(), 1L);

        assertEquals(2, report.getResult().size());
        assertEquals(555.0, report.getSum());
        assertEquals(277.5, report.getAverage(), 0.1);
    }

    @Test
    @DisplayName(value = "Gets Reports for specific driver Money Spent Per Day")
    public void getsReport_MoneySpentPerDay(){

        Date now = new Date();
        Date later = Date.from(Instant.now().plus(2, ChronoUnit.DAYS));

        ReportRequestDTO reportRequestDTO = new ReportRequestDTO("DRIVER", "MONEY_SPENT_PER_DAY", now, later, 1L);
        List<ReportDTO<Double>> reports = new ArrayList<>();
        reports.add(new ReportDTO<>(now, 255.0));
        reports.add(new ReportDTO<>(later, 300.0));

        Mockito.when(this.rideRepository.getTotalCostPerDayForSpecificDriver(reportRequestDTO.getFrom(), reportRequestDTO.getTo(), 1L))
                .thenReturn(reports);

        ReportSumAverageDTO report = this.rideService.getReportForDriver(reportRequestDTO);

        verify(this.rideRepository, times(1)).
                getTotalCostPerDayForSpecificDriver(reportRequestDTO.getFrom(), reportRequestDTO.getTo(), 1L);

        assertEquals(2, report.getResult().size());
        assertEquals(555.0, report.getSum());
        assertEquals(277.5, report.getAverage(), 0.1);
    }

    @Test
    @DisplayName(value = "Gets Driver's Report for kilometers per day")
    public void getsReportForDriver_KilometersPerDay(){

        Date now = new Date();
        Date later = Date.from(Instant.now().plus(2, ChronoUnit.DAYS));

        ReportRequestDTO reportRequestDTO = new ReportRequestDTO("DRIVER", "KILOMETERS_PER_DAY", now, later, 1L);

        Location departure = new Location( 45.24979663860444, 19.85469131685411, "FIRST");
        Location destination = new Location( 45.25694859081531, 19.837455559509213, "SECOND");
        Route route = new Route(departure, destination);
        route.setId(1L);
        Set<Route> locations = new LinkedHashSet<>();
        locations.add(route);

        List<RideLocationWithTimeDTO> rides = new ArrayList<>();
        rides.add(new RideLocationWithTimeDTO(1L, now, locations));
        rides.add(new RideLocationWithTimeDTO(2L, later, locations));


        Mockito.when(this.rideRepository.getRidesWithStartTimeBetweenForSpecificDriver(reportRequestDTO.getFrom(), reportRequestDTO.getTo(), 1L))
                .thenReturn(rides);
        setMocksForKilometersPerDayReport(rides);


        ReportSumAverageDTO report = this.rideService.getReportForDriver(reportRequestDTO);


        verify(this.rideRepository, times(1)).
                getRidesWithStartTimeBetweenForSpecificDriver(reportRequestDTO.getFrom(), reportRequestDTO.getTo(), 1L);

        assertEquals(2, report.getResult().size());
        assertEquals(3.0, report.getSum(), 0.2);
        assertEquals(1.5, report.getAverage(), 0.2);
    }

    @Test
    @DisplayName("Filter reports with valid inputs")
    public void filterReports_withValidInputs() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date date1 = calendar.getTime();
        ReportDTO<Double> report1 = new ReportDTO<Double>(date1, 2056.00);
        calendar.set(2022, Calendar.NOVEMBER, 15, 0, 0, 0);
        Date date3 = calendar.getTime();
        ReportDTO<Double> report3 = new ReportDTO<>(date3, 2445.00);
        calendar.set(2022, Calendar.AUGUST, 27,0, 0, 0);
        Date date2 = calendar.getTime();
        ReportDTO<Double> report2 = new ReportDTO<>(date2,1278.00);
        List<ReportDTO<Double>> list = new ArrayList<>();
        list.add(report1);
        list.add(report2);
        list.add(report3);
        ReportSumAverageDTO dto = this.rideService.filterReports(list, 5);

        assertThat(dto.getAverage()).isEqualTo(1155.8);
        assertThat(dto.getSum()).isEqualTo(5779.0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        assertThat(dto.getResult().get(sdf.parse(sdf.format(date1)))).isEqualTo(2056.00);
        assertThat(dto.getResult().get(sdf.parse(sdf.format(date2)))).isEqualTo(1278.00);
        assertThat(dto.getResult().get(sdf.parse(sdf.format(date3)))).isEqualTo(2445.00);



    }


    @ParameterizedTest
    @ValueSource(longs = {-50L, 0L})
    @DisplayName("Should Throw Exception if totalDays is <= 0")
    public void filterReports_whenTotalDaysIsZero(Long totalDays)  {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date date1 = calendar.getTime();
        ReportDTO<Double> report1 = new ReportDTO<Double>(date1, 2056.00);
        calendar.set(2022, Calendar.NOVEMBER, 15, 0, 0, 0);
        Date date3 = calendar.getTime();
        ReportDTO<Double> report3 = new ReportDTO<>(date3, 2445.00);
        calendar.set(2022, Calendar.AUGUST, 27,0, 0, 0);
        Date date2 = calendar.getTime();
        ReportDTO<Double> report2 = new ReportDTO<>(date2,1278.00);
        List<ReportDTO<Double>> list = new ArrayList<>();
        list.add(report1);
        list.add(report2);
        list.add(report3);

         assertThrows(IllegalArgumentException.class, () -> this.rideService.filterReports(list, totalDays));

    }


    @Test
    @DisplayName("Filter total rides reports with valid inputs")
    public void filterTotalRidesReports_withValidInputs() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date date1 = calendar.getTime();
        ReportDTO<Long> report1 = new ReportDTO<Long>(date1, (long) 2056.00);
        calendar.set(2022, Calendar.NOVEMBER, 15, 0, 0, 0);
        Date date3 = calendar.getTime();
        ReportDTO<Long> report3 = new ReportDTO<>(date3, (long)2445.00);
        calendar.set(2022, Calendar.AUGUST, 27,0, 0, 0);
        Date date2 = calendar.getTime();
        ReportDTO<Long> report2 = new ReportDTO<>(date2,(long)1278.00);
        List<ReportDTO<Long>> list = new ArrayList<>();
        list.add(report1);
        list.add(report2);
        list.add(report3);
        ReportSumAverageDTO dto = this.rideService.filterTotalRidesReports(list, 5L);

        assertThat(dto.getAverage()).isEqualTo(1155.8);
        assertThat(dto.getSum()).isEqualTo(5779.0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        assertThat(dto.getResult().get(sdf.parse(sdf.format(date1)))).isEqualTo(2056.00);
        assertThat(dto.getResult().get(sdf.parse(sdf.format(date2)))).isEqualTo(1278.00);
        assertThat(dto.getResult().get(sdf.parse(sdf.format(date3)))).isEqualTo(2445.00);



    }


    @ParameterizedTest
    @DisplayName("Should Throw Exception when filtering total rides reports, if totalDays <= 0")
    @ValueSource(longs = {0L, -5L})
    public void filterTotalRidesReports_whenTotalDaysIsZero(Long totalDays)  {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JULY, 25, 0, 0, 0);
        Date date1 = calendar.getTime();
        ReportDTO<Long> report1 = new ReportDTO<Long>(date1, (long) 2056.00);
        calendar.set(2022, Calendar.NOVEMBER, 15, 0, 0, 0);
        Date date3 = calendar.getTime();
        ReportDTO<Long> report3 = new ReportDTO<>(date3, (long)2445.00);
        calendar.set(2022, Calendar.AUGUST, 27,0, 0, 0);
        Date date2 = calendar.getTime();
        ReportDTO<Long> report2 = new ReportDTO<>(date2,(long)1278.00);
        List<ReportDTO<Long>> list = new ArrayList<>();
        list.add(report1);
        list.add(report2);
        list.add(report3);

        assertThrows(IllegalArgumentException.class, () -> this.rideService.filterTotalRidesReports(list, totalDays));

    }







    private void setMocksForCancelAndEndRide(Ride ride, Long invalidId) {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passenger);
        Driver driver = new Driver();
        driver.setId(2L);
        driver.setActive(true);
        ride.setVehicleType(new VehicleType(2056, VehicleName.STANDARD));

        Mockito.when(this.rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        Mockito.when(this.driverRepository.findDriverByRidesContaining(ride)).thenReturn(Optional.of(driver));

        Mockito.when(this.rideRepository.findDriverByRideId(1L)).thenReturn(driver);
        Mockito.when(this.rideRepository.findDriverByRideId(invalidId)).thenReturn(null);

        Mockito.when(this.rideRepository.findPassengerByRideId(1L)).thenReturn(passengers);
        Mockito.when(this.rideRepository.findPassengerByRideId(invalidId)).thenReturn(null);


        setMockLocations(ride, invalidId);
        Mockito.when(this.rideRepository.findById(1L)).thenReturn(Optional.of(ride));

    }



    



    private void setMocksForPanicRide(Ride ride, Long invalidId, Long passengerInvalidId,
                                      Passenger passenger, Driver driver) {
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passenger);
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(1L);
        Admin admin = new Admin();
        admin.setId(1L);
        setMocksForFindLocationsByRideId(1L, invalidId);
        Mockito.when(this.rideRepository.findById(1L)).thenReturn(Optional.of(ride));

        Mockito.when(this.rideRepository.getVehicleTypeId(1L)).thenReturn(vehicleType.getId());
        Mockito.when(this.rideRepository.getVehicleTypeId(invalidId)).thenReturn(null);

        Mockito.when(this.userRepository.findAdmin(Role.ADMIN)).thenReturn(admin.getId());
        Mockito.when(this.driverRepository.findDriverByRidesContaining(ride)).thenReturn(Optional.of(driver));

        Mockito.when(this.passengerRepository.findPassengersByRidesContaining(ride)).thenReturn(passengers);

        Mockito.when(this.userRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(passenger));

        Mockito.when(this.vehicleTypeRepository.findById(vehicleType.getId())).thenReturn(Optional.of(vehicleType));

        Mockito.when(this.rideRepository.findDriverByRideId(1L)).thenReturn(driver);
        Mockito.when(this.rideRepository.findDriverByRideId(invalidId)).thenReturn(null);

        Mockito.when(this.rideRepository.findPassengerByRideId(1L)).thenReturn(passengers);
        Mockito.when(this.rideRepository.findPassengerByRideId(invalidId)).thenReturn(null);

        Mockito.when(this.rideRepository.save(ride)).thenReturn(ride);

        setMockLocations(ride, invalidId);
    }

    private void setMockLocations(Ride ride, Long invalidId) {
        Location departure = new Location( 14.44, 15.23, "adresa");
        Location destination = new Location( 15.44, 15.23, "adresa");
        Route route = new Route(departure, destination);
        route.setId(1L);
        LinkedHashSet<Route> locations = new LinkedHashSet<>();
        locations.add(route);

        Mockito.when(this.rideRepository.getLocationsByRide(ride.getId())).thenReturn(locations);
        Mockito.when(this.rideRepository.getLocationsByRide(invalidId)).thenReturn(null);
        Mockito.when(this.rideRepository.getLocationsByRide(ride.getId()))
                .thenReturn(locations);
        Mockito.when(this.routeRepository.getDepartureByRoute(route))
                .thenReturn(Optional.of(departure));
        Mockito.when(this.routeRepository.getDestinationByRoute(route))
                .thenReturn(Optional.of(destination));
    }


    private void setMocksForKilometersPerDayReport(List<RideLocationWithTimeDTO> rides) {
        for(RideLocationWithTimeDTO ride:rides){
            Set<Route> locations = ride.getLocations();
            when(this.rideRepository.getLocationsByRide(ride.getRideId()))
                    .thenReturn((LinkedHashSet<Route>) locations);
            for(Route location: locations){
                when(this.routeRepository.getDepartureByRoute(location))
                        .thenReturn(Optional.of(location.getDeparture()));

                when(this.routeRepository.getDestinationByRoute(location))
                        .thenReturn(Optional.of(location.getDestination()));
            }

        }
    }


    private void setMocksForFilteredRide(Ride ride, Long noDeductionRideId) {
        Driver driver = new Driver();
        driver.setId(1L);
        Driver invalidDriver = new Driver();
        invalidDriver.setId(0L);
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        Set<Passenger> passengers = new LinkedHashSet<>();
        passengers.add(passenger);
        Review review = new Review();
        review.setId(1L);
        Set<Review> reviews = new HashSet<>();
        reviews.add(review);
        Deduction deduction = new Deduction();
        deduction.setId(1L);
        Mockito.when(this.passengerRepository.findPassengersByRidesContaining(ride))
                .thenReturn(passengers);

        Mockito.when(this.reviewRepository.findAllByRideId(ride.getId()))
                .thenReturn(reviews);

        Mockito.when(this.passengerRepository.findbyReviewId(review.getId()))
                .thenReturn(passenger);
        if(ride.getId() == noDeductionRideId)
            Mockito.when(this.deductionRepository.findDeductionByRide(ride))
                    .thenReturn(Optional.empty());

        else
            Mockito.when(this.deductionRepository.findDeductionByRide(ride))
                    .thenReturn(Optional.of(deduction));

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
        Mockito.when(this.userRepository.findById(driver.getId()))
                .thenReturn(Optional.of(driver));
        Mockito.when(this.driverRepository.findDriverByRidesContaining(ride))
                .thenReturn(Optional.of(invalidDriver));
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

    private void setMocksForGettingRides(Date from, Date to) {
        List<Ride> rideList = new ArrayList<>();
        rideList.add(new Ride());
        Page<Ride> pageRides = new PageImpl<>(rideList);

        Mockito.when(rideRepository.findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(4L, from, to, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(this.rideRepository.findAllByDriverId(4L, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(this.rideRepository.findAllByDriverIdAndTimeOfStartAfter(4L, from, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
        Mockito.when(this.rideRepository.findAllByDriverIdAndTimeOfEndBefore(4L, to, PageRequest.of(0, 10)))
                .thenReturn(pageRides);
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
