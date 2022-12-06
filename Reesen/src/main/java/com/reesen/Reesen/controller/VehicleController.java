package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.dto.VehicleDTO;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/vehicle")
public class VehicleController {

    private final IVehicleService vehicleService;

    @Autowired
    public VehicleController(IVehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

<<<<<<< Updated upstream
    @PutMapping(value = "/{vehicle-id}/location")
    public ResponseEntity<LocationDTO> updateLocation(@RequestBody VehicleDTO vehicleDTO, @PathVariable Long vehicleId){
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(Long.parseLong("1"));
        locationDTO.setAddress("Poso Kuca");
        locationDTO.setLongitude(46.123);
        locationDTO.setLatitude(65.028);
        return new ResponseEntity<>(locationDTO, HttpStatus.OK);
=======
    @PutMapping(value = "/{vehicleId}/location")
    public ResponseEntity<String> updateLocation(@RequestBody LocationDTO locationDTO, @PathVariable Long vehicleId){
        return new ResponseEntity<>("Coordinates successfully updated", HttpStatus.OK);
>>>>>>> Stashed changes
    }
}
