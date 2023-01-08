package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.service.interfaces.IDriverService;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "api/ride")
public class RideController {

    private final IRideService rideService;
    private final IDriverService driverService;

    public RideController(IRideService rideService, IDriverService driverService) {
        this.rideService = rideService;
        this.driverService = driverService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> createRide(@RequestBody CreateRideDTO rideDTO){
        RideDTO ride = this.rideService.createRideDTO(rideDTO);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<RideDTO> getDriverActiveRide(@PathVariable("driverId") Long driverId){
        if(driverId < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Ride> ride = this.rideService.findDriverActiveRide(driverId);
        if(ride.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        RideDTO rideDTO = new RideDTO(ride.get());
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<RideDTO> getPassengerActiveRide(@PathVariable("passengerId") Long passengerId){
        if(passengerId < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ride ride = this.rideService.findPassengerActiveRide(passengerId);
        if(ride == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        RideDTO rideDTO = new RideDTO(ride);
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<UserRidesDTO> getRideDetail(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Ride> ride = this.rideService.findOne(id);
        if(ride.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Optional<Driver> driver = this.driverService.findDriverWithRide(ride.get());
        UserRidesDTO rideDTO = this.rideService.getFilteredRide(ride.get(), driver.get().getId());
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/withdraw")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> cancelExistingRide(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id).get();
        ride = this.rideService.withdrawRide(ride);
        this.rideService.save(ride);
        RideDTO withdrawRide = new RideDTO(ride);
        return new ResponseEntity<>(withdrawRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> pressedPanic(@PathVariable Long id, @RequestBody String reason){
        // TODO: get user id from token
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id).get();
        ride = this.rideService.panicRide(ride, reason);
        this.rideService.save(ride);
        RideDTO panicRide = new RideDTO(ride);
        return new ResponseEntity<>(panicRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/accept")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id).get();
        ride = this.rideService.acceptRide(ride);
        this.rideService.save(ride);
        RideDTO acceptedRide = new RideDTO(ride);
        return new ResponseEntity<>(acceptedRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/end")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> endRide(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id).get();
        ride = this.rideService.endRide(ride);
        this.rideService.save(ride);
        RideDTO endedRide = new RideDTO(ride);
        return new ResponseEntity<>(endedRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/cancel")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Long id, @RequestBody String reason){

        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id).get();
        ride = this.rideService.cancelRide(ride, reason);
        this.rideService.save(ride);
        RideDTO canceledRide = new RideDTO(ride);
        return new ResponseEntity<>(canceledRide, HttpStatus.OK);
    }

    @PostMapping(value = "/rides-report")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<ReportSumAverageDTO> getReport(@RequestBody ReportRequestDTO reportRequestDTO){

        ReportSumAverageDTO reportDTO = null;
        if (reportRequestDTO.getRole().equals(Role.ADMIN))
            reportDTO = this.rideService.getReport(reportRequestDTO);
        else if (reportRequestDTO.getRole().equals(Role.DRIVER))
            reportDTO = this.rideService.getReportForDriver(reportRequestDTO);

        if(reportDTO == null) return new ResponseEntity("Bad request!", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(reportDTO, HttpStatus.OK);
    }

}