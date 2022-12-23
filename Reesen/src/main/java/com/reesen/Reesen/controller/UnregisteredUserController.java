package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.DriveAssessmentDTO;
import com.reesen.Reesen.dto.EstimatedTimeDTO;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/unregisteredUser/")
public class UnregisteredUserController {

    @Autowired
    public UnregisteredUserController() {

    }

    @PostMapping
    public ResponseEntity<EstimatedTimeDTO> getAssumption(@RequestBody DriveAssessmentDTO driveAssessment) {
        // TODO: ONLY FORMULA
        EstimatedTimeDTO timeDTO = new EstimatedTimeDTO(10, 450);
        return new ResponseEntity<>(timeDTO, HttpStatus.OK);
    }

}
