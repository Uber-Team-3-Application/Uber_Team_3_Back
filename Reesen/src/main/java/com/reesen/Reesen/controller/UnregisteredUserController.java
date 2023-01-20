package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.DriveAssessmentDTO;
import com.reesen.Reesen.dto.EstimatedTimeDTO;
import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.model.VehicleType;
import com.reesen.Reesen.service.UserService;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<EstimatedTimeDTO> getAssumption(@RequestBody @Valid DriveAssessmentDTO driveAssessment) {


        VehicleType vehicleType = vehicleService.findVehicleTypeByName(VehicleName.getVehicleName(driveAssessment.getVehicleType()));
        EstimatedTimeDTO timeDTO = UserService.getEstimatedTime(driveAssessment, vehicleType);
        return new ResponseEntity<>(timeDTO, HttpStatus.OK);
    }

}
