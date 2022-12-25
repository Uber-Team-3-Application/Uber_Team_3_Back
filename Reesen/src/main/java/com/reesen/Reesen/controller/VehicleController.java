package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.VehicleType;
import com.reesen.Reesen.service.interfaces.IDriverService;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(value = "api/vehicle")
public class VehicleController {

    private final IVehicleService vehicleService;

    @Autowired
    public VehicleController(IVehicleService vehicleService
                             ){
        this.vehicleService = vehicleService;
    }

    @PutMapping(value = "/{vehicleId}/location")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<String> updateLocation(@RequestBody LocationDTO locationDTO, @PathVariable Long vehicleId){
        if(vehicleId < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(this.vehicleService.findOne(vehicleId).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Vehicle vehicle = this.vehicleService.findOne(vehicleId).get();
        vehicle = this.vehicleService.setCurrentLocation(vehicle, locationDTO);
        this.vehicleService.save(vehicle);
        return new ResponseEntity<>("Coordinates successfully updated", HttpStatus.NO_CONTENT);
    }


    @GetMapping(value = "/types")
    public ResponseEntity<List<VehicleType>> getAllVehicleTypes(){
        List<VehicleType> vehicleTypes = this.vehicleService.getVehicleTypes();
        return new ResponseEntity<>(vehicleTypes, HttpStatus.OK);
    }

    @GetMapping(value = "/vehicle-locations")
    public ResponseEntity<List<VehicleLocationWithAvailabilityDTO>> getAllVehicleLocations(){

        List<VehicleLocationWithAvailabilityDTO> locations = this.vehicleService.getAllLocationsWithAvailability();

        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
}
