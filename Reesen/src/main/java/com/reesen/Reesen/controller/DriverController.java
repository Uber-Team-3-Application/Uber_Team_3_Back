package com.reesen.Reesen.controller;


import com.reesen.Reesen.dto.DocumentDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.dto.VehicleDTO;
import com.reesen.Reesen.model.Document;
import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.service.DocumentService;
import com.reesen.Reesen.service.DriverService;
import com.reesen.Reesen.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



//TODO WORKING HOURS !!!!!!!!!!!!!!!!!!!!!!!!
//TODO RIDE !!!!!!!!!!!!!!!!!!!!!!!!


@CrossOrigin
@RestController
@RequestMapping(value = "api/driver")
public class DriverController {
    private final DriverService driverService;
    private final DocumentService documentService;
    private final VehicleService vehicleService;

    @Autowired
    public DriverController(DriverService driverService, DocumentService documentService, VehicleService vehicleService){
        this.driverService = driverService;
        this.documentService = documentService;
        this.vehicleService = vehicleService;
    }


    /**
     *
     *  PUT MAPPINGS
     *
     * **/
    @PutMapping(value = "/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@RequestBody DriverDTO driverDTO, @PathVariable Long id){
        Driver driver = driverService.findOne(id);

        if(driver == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        driver.setPassword(driverDTO.getPassword());
        driver.setEmail(driverDTO.getEmail());
        driver.setName(driverDTO.getName());
        driver.setSurname(driverDTO.getSurname());
        driver.setProfilePicture(driverDTO.getProfilePicture());
        driver.setTelephoneNumber(driverDTO.getTelephoneNumber());
        driver.setAddress(driverDTO.getAddress());

        driver = driverService.save(driver);
        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
    }

            /**
             *
             *  PUT Vehicle
             *
             * **/
    @PutMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> updateVehicle(@RequestBody VehicleDTO vehicleDTO, @PathVariable Long driverId){

        Driver driver = driverService.findOne(driverId);
        if(driver == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Vehicle vehicle = driver.getVehicle();

        if(vehicle == null){
            vehicle = new Vehicle();
        }
        vehicle.setBabyAccessible(vehicleDTO.isBabyTransport());
        vehicle.setPetAccessible(vehicleDTO.isPetTransport());
        vehicle.setCurrentLocation(vehicleDTO.getCurrentLocation());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setRegistrationPlate(vehicleDTO.getLicenseNumber());
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());


        vehicle = this.vehicleService.save(vehicle);
        this.driverService.save(driver);


        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }


    /**
     *
     *  POST MAPPINGS
     *
     * **/
    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO driverDTO){

        Driver driver = new Driver();
        driver.setName(driverDTO.getName());
        driver.setSurname(driverDTO.getSurname());
        driver.setProfilePicture(driverDTO.getProfilePicture());
        driver.setTelephoneNumber(driverDTO.getTelephoneNumber());
        driver.setEmail(driverDTO.getEmail());
        driver.setAddress(driverDTO.getAddress());
        driver.setPassword(driverDTO.getPassword());

        driver = driverService.save(driver);
        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.CREATED);
    }

            /**
             *
             *  POST Documents
             *
             * **/

    @PostMapping(value = "/{id}/documents")
    public ResponseEntity<DocumentDTO> addDocument(@RequestBody DocumentDTO documentDTO, @PathVariable("id") Long driverId){

        // Find driver by id
        Driver driver = this.driverService.findOne(driverId);
        if(driver == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(driver.getId() != driverId) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Document document = new Document();
        document.setDocumentImage(documentDTO.getDocumentImage());
        document.setName(documentDTO.getName());

        // adding driver to document
        document.setDriver(driver);


        // adding document to driver
        driver.getDocuments().add(document);


        //saving both
        this.documentService.save(document);
        this.driverService.save(driver);

        return new ResponseEntity<>(new DocumentDTO(document), HttpStatus.CREATED);

    }

            /**
             *
             *  POST Vehicles
             *
             * **/


    @PostMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> addVehicle(@RequestBody VehicleDTO vehicleDTO, @PathVariable("id") Long driverId){

        // Find driver by id
        Driver driver = this.driverService.findOne(driverId);
        if(driver == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(driver.getId() != driverId) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Vehicle vehicle = new Vehicle();
        vehicle.setBabyAccessible(vehicleDTO.isBabyTransport());
        vehicle.setPetAccessible(vehicleDTO.isPetTransport());
        vehicle.setCurrentLocation(vehicleDTO.getCurrentLocation());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setRegistrationPlate(vehicleDTO.getLicenseNumber());
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());

        // adding driver to document
        vehicle.setDriver(driver);


        // adding document to driver
        driver.setVehicle(vehicle);


        //saving both
        this.vehicleService.save(vehicle);
        this.driverService.save(driver);

        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.CREATED);

    }


    /**
     *
     *  GET MAPPINGS
     *
     * **/

    @GetMapping(value = "/{page}/{size}")
    public ResponseEntity<List<Driver>> getDrivers(@PathVariable Long page, @PathVariable Long size){
        return new ResponseEntity<List<Driver>>(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Long id){

        Driver driver = this.driverService.findOne(id);
        if(driver == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
    }

            /**
             *
             *  GET DOCUMENTS
             *
             * **/

    @GetMapping(value = "/{id}/documents")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable("id") Long id){
        Document document = this.documentService.findOne(id);
        if(document != null) return new ResponseEntity<>(new DocumentDTO(document), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

            /*
            *
            *  GET VEHICLE
            *
            * **/
    @GetMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable("id") Long id){
        Vehicle vehicle = this.vehicleService.findOne(id);
        if(vehicle != null) return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     *
     *  DELETE MAPPINGS
     *
     * **/

    @DeleteMapping(value = "/{id}/documents")
    public ResponseEntity<Void> deleteDocument(@PathVariable("id") Long id){

        Document document = this.documentService.findOne(id);
        if(document != null){
            this.documentService.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
