package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.VehicleType;
import com.reesen.Reesen.service.interfaces.IRideService;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "api/vehicle")
public class VehicleController {

    private final IVehicleService vehicleService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Autowired
    public VehicleController(IVehicleService vehicleService,
                             SimpMessagingTemplate simpMessagingTemplate){
        this.vehicleService = vehicleService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PutMapping(value = "/simulation/{id}")
    public ResponseEntity<String> simulateVehicleMovement(@PathVariable("id") Long rideId){
        this.vehicleService.simulateVehicleByRideId(rideId);
        return new ResponseEntity<>("Simulation", HttpStatus.OK);
    }
    @PutMapping(value = "/{vehicleId}/location")
    public ResponseEntity<VehicleLocationSimulationDTO> updateLocation(@RequestBody @Valid LocationDTO locationDTO, @PathVariable Long vehicleId){

        if(this.vehicleService.findOne(vehicleId).isEmpty()) {
            return new ResponseEntity(
                    "Vehicle does not exist!",HttpStatus.NOT_FOUND);
        }
        Vehicle vehicle = this.vehicleService.findOne(vehicleId).get();
        vehicle = this.vehicleService.setCurrentLocation(vehicle, locationDTO);
        this.vehicleService.save(vehicle);
        VehicleLocationSimulationDTO returnVehicleDTO = new VehicleLocationSimulationDTO(vehicle);
        this.simpMessagingTemplate.convertAndSend("/map-updates/update-vehicle-position", returnVehicleDTO);

        return new ResponseEntity<>(returnVehicleDTO, HttpStatus.OK);
    }

    @GetMapping(value="/{vehicleId}/location")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<LocationDTO> getVehicleLocation(@PathVariable("vehicleId") Long vehicleId){

        Optional<Vehicle> vehicle = this.vehicleService.findOne(vehicleId);
        if(vehicle.isEmpty()) return new ResponseEntity("Vehicle with id does not exist.", HttpStatus.NOT_FOUND);

        LocationDTO locationDTO = this.vehicleService.getCurrentLocation(vehicleId);
        return new ResponseEntity<>(locationDTO, HttpStatus.OK);

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
