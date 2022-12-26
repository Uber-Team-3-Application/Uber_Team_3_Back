package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.DriveAssessmentDTO;
import com.reesen.Reesen.dto.EstimatedTimeDTO;
import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.model.VehicleType;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;

@CrossOrigin
@RestController
@RequestMapping("api/unregisteredUser/")
public class UnregisteredUserController {

    private final IVehicleService vehicleService;
    @Autowired
    public UnregisteredUserController(IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<EstimatedTimeDTO> getAssumption(@RequestBody DriveAssessmentDTO driveAssessment) {

        double amountDistance = 0;
        double estimatedCost = 140; // start
        ArrayList<LocationDTO> locations = new ArrayList<>(driveAssessment.getLocations());
        for (int i = 0; i < locations.size(); i++) {
            for (int j = 1; j < locations.size(); j++) {
                LocationDTO location1 = locations.get(i);
                LocationDTO location2 = locations.get(j);
                double theta = location1.getLongitude() - location2.getLongitude();
                double dist = Math.sin(Math.toRadians(location1.getLatitude())) * Math.sin(Math.toRadians(location2.getLatitude()))
                        + Math.cos(Math.toRadians(location1.getLatitude())) * Math.cos(Math.toRadians(location2.getLatitude())) * Math.cos(Math.toRadians(theta));
                dist = Math.acos(dist);
                dist = Math.toDegrees(dist);
                dist = dist * 60 * 1.1515;
                dist = dist * 1.609344;
                amountDistance += dist;
                VehicleType vehicleType = vehicleService.findVehicleTypeByName(VehicleName.getVehicleName(driveAssessment.getVehicleType().name()));
                estimatedCost += dist * vehicleType.getPricePerKm();

            }
        }

        double estimatedTimeInMinutes = (amountDistance / 80) * 60 * 2;
        EstimatedTimeDTO timeDTO = new EstimatedTimeDTO((int) estimatedTimeInMinutes, (int)estimatedCost);
        return new ResponseEntity<>(timeDTO, HttpStatus.OK);
    }

}
