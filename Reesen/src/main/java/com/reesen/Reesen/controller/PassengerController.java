package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.Passenger.PassengerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/passenger")
public class PassengerController {

    @PostMapping
    public ResponseEntity<PassengerDTO> createPassenger(@RequestBody PassengerDTO passengerDTO){
        return new ResponseEntity<>(new PassengerDTO(), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{activationId}")
    public ResponseEntity<PassengerDTO> activatePassenger(@RequestBody PassengerDTO passengerDTO, @PathVariable Long activationId){
        return new ResponseEntity<>(new PassengerDTO(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PassengerDTO> getPassengers(){
        return new ResponseEntity<>(new PassengerDTO(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PassengerDTO> getPassengerDetails(@PathVariable Long id){
        return new ResponseEntity<>(new PassengerDTO(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<PassengerDTO> getPassengerRides(@PathVariable Long id){
        return new ResponseEntity<>(new PassengerDTO(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PassengerDTO> updatePassenger(@PathVariable Long id){
        return new ResponseEntity<>(new PassengerDTO(), HttpStatus.OK);
    }

}
