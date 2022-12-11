package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.mockup.PassengerMockup;
import com.reesen.Reesen.mockup.PassengerRideMockup;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.paginated.PassengerRidePaginated;
import com.reesen.Reesen.model.paginated.PassengersPaginated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/passenger")
public class PassengerController {

    @PostMapping
    public ResponseEntity<PassengerDTO> createPassenger(@RequestBody PassengerDTO passengerDTO){
        Passenger passenger = new Passenger();
        passenger.setId(Long.parseLong("546"));
        passenger.setName(passengerDTO.getName());
        passenger.setSurname(passengerDTO.getSurname());
        passenger.setProfilePicture(passengerDTO.getProfilePicture());
        passenger.setTelephoneNumber(passengerDTO.getTelephoneNumber());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setAddress(passengerDTO.getAddress());
        passenger.setPassword(passengerDTO.getPassword());
        return new ResponseEntity<>(new PassengerDTO(passenger), HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{activationId}")
    public ResponseEntity<String> activatePassenger(@PathVariable Long activationId){
        return new ResponseEntity<>("Successful account activation", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PassengersPaginated> getPassengers(@RequestParam(value = "page", required = false) int page, @RequestParam(value = "size", required = false) int size){
        PassengersPaginated passengersPaginated = new PassengersPaginated(243);
        passengersPaginated.addPassenger(PassengerMockup.getPassenger());
        return new ResponseEntity<>(passengersPaginated, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PassengerDTO> getPassengerDetails(@PathVariable Long id){
        return new ResponseEntity<>(PassengerMockup.getPassenger(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<PassengerRidePaginated> getPassengerRides(@PathVariable Long id, @RequestParam(value = "page",required = false) int page, @RequestParam(value = "size", required = false) int size,
                                                     @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "from", required = false) String from,  @RequestParam(value = "to", required = false) String to){
        PassengerRidePaginated passengerRidePaginated = new PassengerRidePaginated(243);
        PassengerRideMockup ride = new PassengerRideMockup();
        passengerRidePaginated.addPassengerRide(ride);
        return new ResponseEntity<>(passengerRidePaginated, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PassengerDTO> updatePassenger(@RequestBody PassengerDTO passengerDTO, @PathVariable Long id){
        PassengerDTO passenger = new PassengerDTO();
        passenger.setId(Long.parseLong("546"));
        passenger.setName(passengerDTO.getName());
        passenger.setSurname(passengerDTO.getSurname());
        passenger.setProfilePicture(passengerDTO.getProfilePicture());
        passenger.setTelephoneNumber(passengerDTO.getTelephoneNumber());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setAddress(passengerDTO.getAddress());
        passenger.setPassword(passengerDTO.getPassword());
        return new ResponseEntity<>(passenger, HttpStatus.OK);
    }

}
