package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.dto.PassengerRideDTO;
import com.reesen.Reesen.dto.UserDTO;
import com.reesen.Reesen.dto.UserFullDTO;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.service.interfaces.*;
import com.reesen.Reesen.validation.UserRequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.reesen.Reesen.security.jwt.JwtTokenUtil;

import javax.validation.Valid;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/passenger")
public class PassengerController {

    private final IPassengerService passengerService;
    private final IRideService rideService;
    private final IDriverService driverService;
    private final IDeductionService deductionService;
    private final IRouteService routeService;
    private final UserRequestValidation userRequestValidation;
    private final JwtTokenUtil tokens;

    @Autowired
    public PassengerController(IPassengerService passengerService, IRideService rideService, IDriverService driverService, IDeductionService deductionService, IRouteService routeService, UserRequestValidation userRequestValidation, JwtTokenUtil tokens) {
        this.passengerService = passengerService;
        this.rideService = rideService;
        this.driverService = driverService;
        this.deductionService = deductionService;
        this.routeService = routeService;
        this.userRequestValidation = userRequestValidation;
        this.tokens = tokens;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public ResponseEntity<PassengerDTO> createPassenger(@RequestBody @Valid PassengerDTO passengerDTO){
        if(this.passengerService.findByEmail(passengerDTO.getEmail()) != null)
            return new ResponseEntity(
                    new ErrorResponseMessage("User with that email already exists!"),
                    HttpStatus.BAD_REQUEST
            );
        PassengerDTO passenger = this.passengerService.createPassengerDTO(passengerDTO);
        return new ResponseEntity<>(passenger, HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{activationId}")
    public ResponseEntity<String> activatePassenger(@PathVariable Long activationId){
        VerificationToken verificationToken = new VerificationToken(activationId);
        this.passengerService.saveVerificationToken(verificationToken);
        return new ResponseEntity<>(verificationToken.getUrl(), HttpStatus.OK);
    }

    @GetMapping(value = "/activate/account")
    public ResponseEntity<String> activatePassengerAccount(@RequestParam String url){
        VerificationToken verificationToken = this.passengerService.findByUrl(url);
        if(verificationToken == null) return new ResponseEntity("Not found!", HttpStatus.NOT_FOUND);
        if(verificationToken.getExpirationDate().isBefore(LocalDateTime.now()))
            return new ResponseEntity(new ErrorResponseMessage("Activation expired!"), HttpStatus.BAD_REQUEST);
        passengerService.activateAccount(verificationToken.getPassengerId());
        return new ResponseEntity<>("Successful account activation", HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<Paginated<PassengerDTO>> getPassengers(Pageable page){
        Page<Passenger> passengers = this.passengerService.findAll(page);
        Set<PassengerDTO> passengerDTOs = new HashSet<>();
        for(Passenger passenger: passengers){
            passengerDTOs.add(new PassengerDTO(passenger));
        }
        return new ResponseEntity<>(new Paginated<>(passengers.getNumberOfElements(), passengerDTOs), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER', 'DRIVER')")
    public ResponseEntity<UserFullDTO> getPassengerDetails(@PathVariable Long id){

        Optional<Passenger> passenger = this.passengerService.findOne(id);
        if(passenger.isEmpty())
            return new ResponseEntity("Passenger does not exist!", HttpStatus.NOT_FOUND);
        UserFullDTO passengerDTO = new UserFullDTO(passenger.get());
        return new ResponseEntity<>(passengerDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/ride")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public ResponseEntity<Paginated<PassengerRideDTO>> getPassengerRides(@PathVariable Long id,
                                                                         Pageable page,
                                                                         @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
                                                                         @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to,
                                                                         @RequestHeader Map<String, String> headers) {

        String role = this.userRequestValidation.getRoleFromToken(headers);
        if(role.equalsIgnoreCase("passenger")){
            boolean areIdsEqual = this.userRequestValidation.areIdsEqual(headers, id);
            if(!areIdsEqual) return new ResponseEntity("Passenger does not exist!", HttpStatus.NOT_FOUND);
        }
        Optional<Passenger> passenger = this.passengerService.findOne(id);
        if (passenger.isEmpty()) return new ResponseEntity("Passenger does not exist!", HttpStatus.NOT_FOUND);
        Date dateFrom = null;
        Date dateTo = null;

        if (from != null || to != null) {
            if  (from != null) {
                dateFrom = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            if (to != null) {
                dateTo = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        }
        Page<Ride> passengerRides = this.rideService.findAllRidesForPassenger(id, page, dateFrom, dateTo);

        LinkedHashSet<PassengerRideDTO> rideDTOs = new LinkedHashSet<>();

        for (Ride ride : passengerRides) {
            ride.setDriver(driverService.findDriverByRidesContaining(ride));
            ride.setPassengers(passengerService.findPassengersByRidesContaining(ride));
            ride.setDeduction(deductionService.findDeductionByRide(ride).orElse(new Deduction()));
            LinkedHashSet<Route> locations;
            locations = rideService.getLocationsByRide(ride.getId());
            for (Route location : locations) {
                location.setDestination(this.routeService.getDestinationByRoute(location).get());
                location.setDeparture(this.routeService.getDepartureByRoute(location).get());
            }
            ride.setLocations(locations);
            rideDTOs.add(new PassengerRideDTO(ride));
        }
        return new ResponseEntity<>(new Paginated<>(passengerRides.getNumberOfElements(), rideDTOs), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public ResponseEntity<PassengerDTO> updatePassenger(@RequestBody @Valid PassengerDTO passengerDTO,
                                                        @PathVariable Long id,
                                                        @RequestHeader Map<String, String> headers){
        String role = this.userRequestValidation.getRoleFromToken(headers);
        if(role.equalsIgnoreCase("passenger")){
            boolean areIdsEqual = this.userRequestValidation.areIdsEqual(headers, id);
            if(!areIdsEqual) return new ResponseEntity("Passenger does not exist!", HttpStatus.NOT_FOUND);
        }
        if(this.passengerService.findOne(id).isEmpty())
            return new ResponseEntity("Passenger does not exist!", HttpStatus.NOT_FOUND);
        Passenger passenger = this.passengerService.findByEmail(passengerDTO.getEmail());
        if(passenger != null && !passenger.getId().toString().equals(id.toString())){
            return new ResponseEntity("Invalid data. For example Bad email format.", HttpStatus.BAD_REQUEST);
        }
        passenger = this.passengerService.getPassengerFromPassengerDTO(id, passengerDTO);
        passenger = this.passengerService.save(passenger);
        PassengerDTO updatedPassenger = new PassengerDTO(passenger);
        return new ResponseEntity<>(updatedPassenger, HttpStatus.OK);
    }

}
