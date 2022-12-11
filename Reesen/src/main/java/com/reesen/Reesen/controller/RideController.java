package com.reesen.Reesen.controller;

<<<<<<< Updated upstream
import com.reesen.Reesen.dto.PanicRideDTO;
import com.reesen.Reesen.dto.Ride.*;
=======
import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.dto.CreateRideDTO;
import com.reesen.Reesen.dto.RideDTO;
import com.reesen.Reesen.dto.RidePanicDTO;
import com.reesen.Reesen.mockup.RideMockup;
import com.reesen.Reesen.mockup.RidePanicMockup;
import com.reesen.Reesen.model.Driver;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO rideDTO){
        return new ResponseEntity<>(new RideDTO(), HttpStatus.CREATED);
=======
    public ResponseEntity<RideMockup> createRide(@RequestBody CreateRideDTO rideDTO){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
    @PutMapping(value = "/{id}")
    public ResponseEntity<CancelExistingRideDTO> cancelExistingRide(@PathVariable Long id){
        return new ResponseEntity<>(new CancelExistingRideDTO(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic")
    public ResponseEntity<RidePanicDTO> pressedPanic(@PathVariable Long id){
        return new ResponseEntity<>(new RidePanicDTO(), HttpStatus.OK);
=======
    @PutMapping(value = "/{id}/withdraw")
    public ResponseEntity<RideMockup> cancelExistingRide(@PathVariable Long id){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic")
    public ResponseEntity<RidePanicMockup> pressedPanic(@PathVariable Long id, @RequestBody String reason){
        return new ResponseEntity<>(new RidePanicMockup(), HttpStatus.OK);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    public ResponseEntity<CancelRideDTO> cancelRide(@PathVariable Long id){
        return new ResponseEntity<>(new CancelRideDTO(), HttpStatus.OK);
=======
    public ResponseEntity<RideMockup> cancelRide(@PathVariable Long id, @RequestBody String reason){
        return new ResponseEntity<>(new RideMockup(), HttpStatus.OK);
>>>>>>> Stashed changes
    }
}
