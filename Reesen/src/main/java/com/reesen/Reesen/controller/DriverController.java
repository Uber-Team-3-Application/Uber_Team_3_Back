package com.reesen.Reesen.controller;


import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/driver")
public class DriverController {
    private DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService){
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody Driver driver){
        Driver newDriver = this.driverService.add(driver);
        return new ResponseEntity<Driver>(newDriver, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{page}/{size}")
    public ResponseEntity<List<Driver>> getDrivers(@PathVariable Integer page, @PathVariable Integer size){
        return new ResponseEntity<List<Driver>>(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Driver> getDriver(@PathVariable Integer id){
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Driver> updateDriver(@RequestBody Driver driver, @PathVariable Long id){
        Driver updatedDriver = driverService.findOne(id);
        /*
        *  Here we set data
        * */
        updatedDriver  = driverService.update(updatedDriver);
        return new ResponseEntity<>(updatedDriver, HttpStatus.OK);
    }

}
