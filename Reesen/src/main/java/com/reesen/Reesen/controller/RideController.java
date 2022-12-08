package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.PanicRideDTO;
import com.reesen.Reesen.dto.Ride.*;
import com.reesen.Reesen.mockup.RideMockup;
import com.reesen.Reesen.model.Ride;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/ride")
public class RideController {

    @PostMapping
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO rideDTO){
        Ride ride = new Ride();
        return new ResponseEntity<>(new RideDTO(ride), HttpStatus.CREATED);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<RideMockup> getDriverActiveRide(@PathVariable("driverId") Long driverId){
        RideMockup ride = new RideMockup();
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<RideMockup> getPassengerActiveRide(@PathVariable("passengerId") Long passengerId){
        RideMockup ride = new RideMockup();
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RideMockup> getRideDetail(@PathVariable Long id){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<RideMockup> cancelExistingRide(@PathVariable Long id){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic")
    public ResponseEntity<RidePanicDTO> pressedPanic(@PathVariable Long id, @PathVariable String reason){
        return new ResponseEntity<>(new RidePanicDTO(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<RideMockup> acceptRide(@PathVariable Long id){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/end")
    public ResponseEntity<RideMockup> endRide(@PathVariable Long id){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/cancel")
    public ResponseEntity<RideDTO> cancelRide(@RequestBody RideDTO rideDTO, @PathVariable Long id, @PathVariable String reason){
        RideDTO ride = new RideDTO();
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }
}
