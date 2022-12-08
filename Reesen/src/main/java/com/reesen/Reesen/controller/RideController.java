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
        //ride.setRoutes(rideDTO.getRoutes());
        //ride.setPassengers(rideDTO.getPassengers());
        ride.setVehicleType(rideDTO.getVehicleType());
        ride.setBabyAccessible(rideDTO.isBabyAccessible());
        ride.setPetAccessible(rideDTO.isPetAccessible());
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
        ride.setId(Long.parseLong("546"));
        ride.setTimeOfStart(rideDTO.getTimeOfStart());
        ride.setTimeOfEnd(rideDTO.getTimeOfEnd());
        ride.setTotalPrice(rideDTO.getTotalPrice());
        ride.setDriverId(rideDTO.getDriverId());
       // ride.setPassengers(rideDTO.getPassengers());
        ride.setEstimatedTime(rideDTO.getEstimatedTime());
        ride.setVehicleType(rideDTO.getVehicleType());
        ride.setBabyAccessible(rideDTO.isBabyAccessible());
        ride.setPetAccessible(rideDTO.isPetAccessible());
       // ride.setRejections(rideDTO.getRejections());
      //  ride.setLocations(rideDTO.getLocations());
        ride.setStatus(rideDTO.getStatus());
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }
}
