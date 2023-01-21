package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Deduction;
import com.reesen.Reesen.model.ErrorResponseMessage;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.service.interfaces.IDriverService;
import com.reesen.Reesen.service.interfaces.IFavoriteRideService;
import com.reesen.Reesen.service.interfaces.IRideService;
import com.reesen.Reesen.validation.UserRequestValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(value = "api/ride")
public class RideController {

    private final IRideService rideService;
    private final IFavoriteRideService favoriteRideService;
    private final IDriverService driverService;
    private final UserRequestValidation userRequestValidation;

    public RideController(IRideService rideService, IFavoriteRideService favoriteRideService, IDriverService driverService, UserRequestValidation userRequestValidation) {
        this.rideService = rideService;
        this.favoriteRideService = favoriteRideService;
        this.driverService = driverService;
        this.userRequestValidation = userRequestValidation;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseEntity<RideDTO> createRide(@Valid @Nullable @RequestBody CreateRideDTO rideDTO, @RequestHeader Map<String, String> headers){
        String role = this.userRequestValidation.getRoleFromToken(headers);
        if(!role.equalsIgnoreCase("passenger")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (rideDTO == null || this.rideService.validateCreateRideDTO(rideDTO)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (this.rideService.checkForPendingRide(userRequestValidation.getIdFromToken(headers))) return new ResponseEntity(new ErrorResponseMessage("Cannot create a ride while you have one already pending!"), HttpStatus.BAD_REQUEST);
        RideDTO ride = this.rideService.createRideDTO(rideDTO, userRequestValidation.getIdFromToken(headers));
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    public ResponseEntity<RideDTO> getDriverActiveRide(@PathVariable("driverId") Long driverId, @RequestHeader Map<String, String> headers){
       if(driverId < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ride ride = this.rideService.findDriverActiveRide(driverId);
        if(ride == null)
            return new ResponseEntity("Active ride does not exist", HttpStatus.NOT_FOUND);
        if(!driverId.equals(userRequestValidation.getIdFromToken(headers))) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        RideDTO rideDTO = new RideDTO(ride);
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public ResponseEntity<RideDTO> getPassengerActiveRide(@PathVariable("passengerId") Long passengerId, @RequestHeader Map<String, String> headers){
        if(passengerId < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ride ride = this.rideService.findPassengerActiveRide(passengerId);
        if(ride == null)
            return new ResponseEntity("Active ride does not exist", HttpStatus.NOT_FOUND);
        if(!passengerId.equals(userRequestValidation.getIdFromToken(headers))) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        RideDTO rideDTO = new RideDTO(ride);
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<RideDTO> getRideDetail(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ride ride = this.rideService.findOne(id);
        if(ride == null)
            return new ResponseEntity("Ride does not exist", HttpStatus.NOT_FOUND);
        RideDTO rto = new RideDTO(ride);
        return new ResponseEntity<>(rto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/withdraw")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseEntity<RideDTO> cancelExistingRide(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);
        RideDTO withdrawRide = this.rideService.withdrawRide(id);
        return new ResponseEntity<>(withdrawRide, HttpStatus.OK);
    }


    @PutMapping(value = "/{id}/panic")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseEntity<RideDTO> pressedPanic(@PathVariable Long id, @Nullable @RequestBody ReasonDTO reason, @RequestHeader Map<String, String> headers){
        String role = this.userRequestValidation.getRoleFromToken(headers);
        if(!role.equalsIgnoreCase("passenger"))   return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(this.rideService.findOne(id) == null)
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);
        if(reason == null)
            return new ResponseEntity("Must give a reason!", HttpStatus.BAD_REQUEST);
        Ride ride = this.rideService.findOne(id);
        RideDTO panicRide  = this.rideService.panicRide(id, reason.getReason(), this.userRequestValidation.getIdFromToken(headers));
        return new ResponseEntity<>(panicRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/accept")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Long id){
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);
        RideDTO acceptedRide = this.rideService.acceptRide(id);
        return new ResponseEntity<>(acceptedRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/start")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> startRide(@PathVariable Long id, @RequestHeader Map<String, String> headers){
        String role = this.userRequestValidation.getRoleFromToken(headers);
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);
        if(!role.equalsIgnoreCase("driver"))// ||
               // !(this.driverService.findDriverByRidesContaining(this.rideService.findOne(id)).getId() == this.userRequestValidation.getIdFromToken(headers)) )
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        RideDTO startedRide = this.rideService.startRide(id);
        return new ResponseEntity<>(startedRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/end")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> endRide(@PathVariable Long id, @RequestHeader Map<String, String> headers){
        String role = this.userRequestValidation.getRoleFromToken(headers);
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);
        if(!role.equalsIgnoreCase("driver"))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        RideDTO endedRide = this.rideService.endRide(id);
        return new ResponseEntity<>(endedRide, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/cancel")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Long id, @Nullable @RequestBody ReasonDTO reason, @RequestHeader Map<String, String> headers){
        String role = this.userRequestValidation.getRoleFromToken(headers);
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.rideService.findOne(id) == null)
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);
        if(!role.equalsIgnoreCase("driver"))
              //  !(this.driverService.findDriverByRidesContaining(this.rideService.findOne(id)).getId() == this.userRequestValidation.getIdFromToken(headers)) )
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if(reason == null)
            return new ResponseEntity("Must give a reason!", HttpStatus.BAD_REQUEST);
        RideDTO canceledRide = this.rideService.cancelRide(id, reason.getReason());
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

    @PostMapping(value = "/favorites", consumes = "application/json")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseEntity<FavoriteRideDTO> addFavouriteRide(@Valid @Nullable @RequestBody CreateFavoriteRideDTO  favouriteRide, @RequestHeader Map<String, String> headers){
        if(favouriteRide == null || this.favoriteRideService.validateRideDTO(favouriteRide))  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        FavoriteRideDTO response = this.favoriteRideService.addFavouriteRide(favouriteRide, this.userRequestValidation.getIdFromToken(headers));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/favorites")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseEntity<Set<FavoriteRideDTO>> getFavouriteRides(@RequestHeader Map<String, String> headers)
    {
        Set<FavoriteRideDTO> response = this.favoriteRideService.getFavouriteRides(userRequestValidation.getIdFromToken(headers));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping(value = "/favorites/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseEntity<String> deleteFavouriteRides(@PathVariable Long id, @RequestHeader Map<String, String> headers)
    {
        if(id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.favoriteRideService.findOne(id) == null)
            return new ResponseEntity("Favorite location does not exist!", HttpStatus.NOT_FOUND);
        this.favoriteRideService.deleteFavouriteRides(id, this.userRequestValidation.getIdFromToken(headers));
        return new ResponseEntity<>("Successful deletion of favorite location!",HttpStatus.NO_CONTENT);
    }


}