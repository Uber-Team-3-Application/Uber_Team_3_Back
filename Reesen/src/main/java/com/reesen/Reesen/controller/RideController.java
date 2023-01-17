package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.ErrorResponseMessage;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.service.interfaces.IDriverService;
import com.reesen.Reesen.service.interfaces.IRideService;
import com.reesen.Reesen.validation.UserRequestValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "api/ride")
public class RideController {

    private final IRideService rideService;
    private final IDriverService driverService;
    private final UserRequestValidation userRequestValidation;

    public RideController(IRideService rideService, IDriverService driverService, UserRequestValidation userRequestValidation) {
        this.rideService = rideService;
        this.driverService = driverService;
        this.userRequestValidation = userRequestValidation;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public ResponseEntity<RideDTO> createRide(@Valid @RequestBody CreateRideDTO rideDTO, @RequestHeader Map<String, String> headers){
        String role = this.userRequestValidation.getRoleFromToken(headers);
        if(role.equalsIgnoreCase("admin")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (this.rideService.validateCreateRideDTO(rideDTO)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (this.rideService.checkForPendingRide(userRequestValidation.getIdFromToken(headers))) return new ResponseEntity(new ErrorResponseMessage("Cannot create a ride while you have one already pending!"), HttpStatus.BAD_REQUEST);
        RideDTO ride = this.rideService.createRideDTO(rideDTO, userRequestValidation.getIdFromToken(headers));
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    public ResponseEntity<RideDTO> getDriverActiveRide(@PathVariable("driverId") Long driverId, @RequestHeader Map<String, String> headers){
       if(driverId < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!driverId.equals(userRequestValidation.getIdFromToken(headers))) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Ride ride = this.rideService.findDriverActiveRide(driverId);
        if(ride == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        RideDTO rideDTO = new RideDTO(ride);
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public ResponseEntity<RideDTO> getPassengerActiveRide(@PathVariable("passengerId") Long passengerId, @RequestHeader Map<String, String> headers){
        if(!passengerId.equals(userRequestValidation.getIdFromToken(headers))) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if(passengerId < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ride ride = this.rideService.findPassengerActiveRide(passengerId);
        if(ride == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        RideDTO rideDTO = new RideDTO(ride);
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
//    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<UserRidesDTO> getRideDetail(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ride ride = this.rideService.findOne(id);
        if(ride == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Optional<Driver> driver = this.driverService.findDriverWithRide(ride);
        UserRidesDTO rideDTO = this.rideService.getFilteredRide(ride, driver.get().getId());
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/withdraw")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> cancelExistingRide(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id);
        ride = this.rideService.withdrawRide(ride);
        this.rideService.save(ride);
        RideDTO withdrawRide = new RideDTO(ride);
        return new ResponseEntity<>(withdrawRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> pressedPanic(@PathVariable Long id, @Valid @RequestBody String reason){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long userId = Long.valueOf(request.getHeader("Id"));
        ride = this.rideService.panicRide(ride, reason, userId);
        this.rideService.save(ride);
        RideDTO panicRide = new RideDTO(ride);
        return new ResponseEntity<>(panicRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/accept")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id);
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
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id);
        ride = this.rideService.endRide(ride);
        this.rideService.save(ride);
        RideDTO endedRide = new RideDTO(ride);
        return new ResponseEntity<>(endedRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/cancel")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Long id, @Valid @RequestBody String reason){

        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ride ride = this.rideService.findOne(id);
        ride = this.rideService.cancelRide(ride, reason);
        this.rideService.save(ride);
        RideDTO canceledRide = new RideDTO(ride);
        return new ResponseEntity<>(canceledRide, HttpStatus.OK);
    }

    @PostMapping(value = "/rides-report")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<ReportSumAverageDTO> getReport(@Valid @RequestBody ReportRequestDTO reportRequestDTO){

        ReportSumAverageDTO reportDTO = null;
        if (reportRequestDTO.getRole().equals(Role.ADMIN))
            reportDTO = this.rideService.getReport(reportRequestDTO);
        else if (reportRequestDTO.getRole().equals(Role.DRIVER))
            reportDTO = this.rideService.getReportForDriver(reportRequestDTO);

        if(reportDTO == null) return new ResponseEntity("Bad request!", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(reportDTO, HttpStatus.OK);
    }

}