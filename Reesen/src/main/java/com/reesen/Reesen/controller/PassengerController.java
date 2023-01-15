package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.dto.PassengerRideDTO;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.reesen.Reesen.security.jwt.JwtTokenUtil;

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

    private final JwtTokenUtil tokens;

    @Autowired
    public PassengerController(IPassengerService passengerService, IRideService rideService, IDriverService driverService, IDeductionService deductionService, IRouteService routeService, JwtTokenUtil tokens) {
        this.passengerService = passengerService;
        this.rideService = rideService;
        this.driverService = driverService;
        this.deductionService = deductionService;
        this.routeService = routeService;
        this.tokens = tokens;
    }

    @PostMapping
    public ResponseEntity<PassengerDTO> createPassenger(@RequestBody PassengerDTO passengerDTO){
        if(this.passengerService.findByEmail(passengerDTO.getEmail()) != null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
        if(verificationToken.getExpirationDate().isBefore(LocalDateTime.now()))
            return new ResponseEntity<>("Activation expired!", HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>(new Paginated<PassengerDTO>(passengers.getNumberOfElements(), passengerDTOs), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<PassengerDTO> getPassengerDetails(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Passenger> passenger = this.passengerService.findOne(id);
        if(passenger.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        PassengerDTO passengerDTO = new PassengerDTO(passenger.get());
        return new ResponseEntity<>(passengerDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/ride")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public ResponseEntity<Paginated<PassengerRideDTO>> getPassengerRides(@PathVariable Long id,
                                                                         Pageable page,
                                                                         @RequestParam(value = "sort", required = false) String sort,
                                                                         @RequestParam(value = "from", required = false) String from,
                                                                         @RequestParam(value = "to", required = false) String to) throws ParseException {
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Passenger> passenger = this.passengerService.findOne(id);
        if (passenger.isEmpty()) return new ResponseEntity("Passenger does not exist!", HttpStatus.NOT_FOUND);
        Page<Ride> rides = this.rideService.findAllRidesForPassenger(id, page, null, null);
        Date dateFrom = null;
        Date dateTo = null;
        if (from != null || to != null) {
            if  (from != null) {
                LocalDate date = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
                dateFrom = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            if (to != null) {
                LocalDate date = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);
                dateTo = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
    public ResponseEntity<PassengerDTO> updatePassenger(@RequestBody PassengerDTO passengerDTO, @PathVariable Long id){
        if(this.passengerService.findOne(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Passenger passenger = this.passengerService.findByEmail(passengerDTO.getEmail());
        if(passenger != null && !passenger.getId().toString().equals(id.toString())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        passenger = this.passengerService.getPassengerFromPassengerDTO(id, passengerDTO);
        passenger = this.passengerService.save(passenger);
        PassengerDTO updatedPassenger = new PassengerDTO(passenger);
        return new ResponseEntity<>(updatedPassenger, HttpStatus.OK);
    }

}
