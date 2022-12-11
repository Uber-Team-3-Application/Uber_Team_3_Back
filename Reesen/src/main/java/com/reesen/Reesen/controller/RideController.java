package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.PanicRideDTO;
import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.dto.CreateRideDTO;
import com.reesen.Reesen.dto.RideDTO;
import com.reesen.Reesen.dto.RidePanicDTO;
import com.reesen.Reesen.mockup.RideMockup;
import com.reesen.Reesen.mockup.RidePanicMockup;
import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.model.Ride;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;

@CrossOrigin
@RestController
@RequestMapping(value = "api/ride")
public class RideController {

    @PostMapping
    public ResponseEntity<RideMockup> createRide(@RequestBody CreateRideDTO rideDTO){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<RideMockup> getDriverActiveRide(@PathVariable("driverId") Long driverId){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<RideMockup> getPassengerActiveRide(@PathVariable("passengerId") Long passengerId){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RideDTO> getRideDetail(@PathVariable Long id){
        return new ResponseEntity<>(new RideDTO(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/withdraw")
    public ResponseEntity<RideMockup> cancelExistingRide(@PathVariable Long id){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic")
    public ResponseEntity<RidePanicMockup> pressedPanic(@PathVariable Long id, @RequestBody String reason){
        return new ResponseEntity<>(new RidePanicMockup(), HttpStatus.OK);
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
    public ResponseEntity<RideMockup> cancelRide(@PathVariable Long id, @RequestBody String reason){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }
}
