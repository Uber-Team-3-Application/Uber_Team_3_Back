package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.PanicRideDTO;
import com.reesen.Reesen.dto.Ride.*;
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
        return new ResponseEntity<>(new RideDTO(), HttpStatus.CREATED);
    }

    @GetMapping(value = "/active/{driverId}")
    public ResponseEntity<DriverActiveRideDTO> getDriverActiveRide(@PathVariable("driverId") Long driverId){
        return new ResponseEntity<>(new DriverActiveRideDTO(), HttpStatus.OK);
    }

    @GetMapping(value = "/active/{passengerId}")
    public ResponseEntity<PassengerActiveRideDTO> getPassengerActiveRide(@PathVariable("passengerId") Long passengerId){
        return new ResponseEntity<>(new PassengerActiveRideDTO(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RideDTO> getRideDetail(@PathVariable Long id){
        return new ResponseEntity<>(new RideDTO(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CancelExistingRideDTO> cancelExistingRide(@PathVariable Long id){
        return new ResponseEntity<>(new CancelExistingRideDTO(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic")
    public ResponseEntity<RidePanicDTO> pressedPanic(@PathVariable Long id){
        return new ResponseEntity<>(new RidePanicDTO(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<AcceptRideDTO> acceptRide(@PathVariable Long id){
        return new ResponseEntity<>(new AcceptRideDTO(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/end")
    public ResponseEntity<EndRideDTO> endRide(@PathVariable Long id){
        return new ResponseEntity<>(new EndRideDTO(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/cancel")
    public ResponseEntity<CancelRideDTO> cancelRide(@PathVariable Long id){
        return new ResponseEntity<>(new CancelRideDTO(), HttpStatus.OK);
    }
}
