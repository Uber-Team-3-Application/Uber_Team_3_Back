package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.dto.RideDTO;
import com.reesen.Reesen.mockup.PassengerRideMockup;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.model.paginated.PassengerRidePaginated;
import com.reesen.Reesen.service.interfaces.IPassengerService;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/passenger")
public class PassengerController {

    private final IPassengerService passengerService;
    private final IRideService rideService;

    @Autowired
    public PassengerController(IPassengerService passengerService, IRideService rideService) {
        this.passengerService = passengerService;
        this.rideService = rideService;
    }

    @PostMapping
    public ResponseEntity<PassengerDTO> createPassenger(@RequestBody PassengerDTO passengerDTO){
        if(this.passengerService.findByEmail(passengerDTO.getEmail()) != null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        PassengerDTO passenger = this.passengerService.createPassengerDTO(passengerDTO);
        return new ResponseEntity<>(passenger, HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{activationId}")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<String> activatePassenger(@PathVariable Long activationId){
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
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<Paginated<RideDTO>> getPassengerRides(@PathVariable Long id, Pageable page, @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to) throws ParseException {
        Date fromDate =new SimpleDateFormat("dd/MM/yyyy").parse(from);
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.passengerService.findOne(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Passenger passenger = this.passengerService.findOne(id).get();
        Set<Ride> rides = passenger.getRides();
        Set<RideDTO> rideDTOs = new HashSet<>();
        for(Ride ride: rides){
            rideDTOs.add(new RideDTO(ride));
        }
        return new ResponseEntity<>(new Paginated<RideDTO>(page.getPageNumber(), rideDTOs), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<PassengerDTO> updatePassenger(@RequestBody PassengerDTO passengerDTO, @PathVariable Long id){
        if(this.passengerService.findOne(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Passenger passenger = this.passengerService.findByEmail(passengerDTO.getEmail());
        if(passenger != null && !passenger.getId().equals(id.toString())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        passenger = this.passengerService.getPassengerFromPassengerDTO(id, passengerDTO);
        this.passengerService.save(passenger);
        PassengerDTO updatedPassenger = new PassengerDTO(passenger);
        return new ResponseEntity<>(updatedPassenger, HttpStatus.OK);
    }

}
