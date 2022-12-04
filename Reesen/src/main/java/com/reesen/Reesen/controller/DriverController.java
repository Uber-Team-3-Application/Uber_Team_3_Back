package com.reesen.Reesen.controller;


import com.reesen.Reesen.dto.DocumentDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.dto.VehicleDTO;
import com.reesen.Reesen.dto.WorkingHoursDTO;
import com.reesen.Reesen.mockup.DocumentMockup;
import com.reesen.Reesen.mockup.DriverMockup;
import com.reesen.Reesen.mockup.DriverRideMockup;
import com.reesen.Reesen.mockup.VehicleMockup;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.paginated.DriverPaginated;
import com.reesen.Reesen.model.paginated.DriverRidePaginated;
import com.reesen.Reesen.model.paginated.WorkingHoursPaginated;
import com.reesen.Reesen.service.interfaces.IDocumentService;
import com.reesen.Reesen.service.interfaces.IDriverService;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import com.reesen.Reesen.service.interfaces.IWorkingHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;


//TODO WORKING HOURS !!!!!!!!!!!!!!!!!!!!!!!!
//TODO RIDE !!!!!!!!!!!!!!!!!!!!!!!!


@CrossOrigin
@RestController
@RequestMapping(value = "api/driver")
public class DriverController {
    private final IDriverService driverService;
    private final IDocumentService documentService;
    private final IVehicleService vehicleService;
    private final IWorkingHoursService workingHoursService;

    @Autowired
    public DriverController(IDriverService driverService,
                            IDocumentService documentService,
                            IVehicleService vehicleService,
                            IWorkingHoursService workingHoursService){
        this.driverService = driverService;
        this.documentService = documentService;
        this.vehicleService = vehicleService;
        this.workingHoursService = workingHoursService;
    }


    /**
     *
     *  PUT MAPPINGS
     *
     * **/
    @PutMapping(value = "/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@RequestBody DriverDTO driverDTO, @PathVariable Long id){
        //Driver driver = driverService.findOne(id);

        //if(driver == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        DriverDTO driver = new DriverDTO();

        driver.setPassword(driverDTO.getPassword());
        driver.setEmail(driverDTO.getEmail());
        driver.setName(driverDTO.getName());
        driver.setSurname(driverDTO.getSurname());
        driver.setProfilePicture(driverDTO.getProfilePicture());
        driver.setTelephoneNumber(driverDTO.getTelephoneNumber());
        driver.setAddress(driverDTO.getAddress());

        driver.setId(Long.parseLong("123"));

        //driver = driverService.save(driver);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

            /**
             *
             *  PUT Vehicle
             *
             * **/
    @PutMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> updateVehicle(@RequestBody VehicleDTO vehicleDTO, @PathVariable("id") Long driverId){

       /* Driver driver = driverService.findOne(driverId);
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
        this.driverService.save(driver);*/


        return new ResponseEntity<>(VehicleMockup.getVehicleDTO(), HttpStatus.OK);
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
        driver.setId(Long.parseLong("123"));
        // driver = driverService.save(driver);
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
        /*Driver driver = this.driverService.findOne(driverId);
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
        this.driverService.save(driver);*/

        return new ResponseEntity<>(DocumentMockup.getDocumentDTO(), HttpStatus.OK);

    }

            /**
             *
             *  POST Vehicles
             *
             * **/


    @PostMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> addVehicle(@RequestBody VehicleDTO vehicleDTO, @PathVariable("id") Long driverId){

        // Find driver by id
        //Driver driver = this.driverService.findOne(driverId);
        //if(driver == null || driver.getId() != driverId) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

/*        Vehicle vehicle = new Vehicle();
        vehicle.setBabyAccessible(vehicleDTO.isBabyTransport());
        vehicle.setPetAccessible(vehicleDTO.isPetTransport());
        vehicle.setCurrentLocation(vehicleDTO.getCurrentLocation());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setRegistrationPlate(vehicleDTO.getLicenseNumber());
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());*/

        // adding driver to document
        //vehicle.setDriver(driver);


        // adding document to driver
        //driver.setVehicle(vehicle);


        //saving both
        //this.vehicleService.save(vehicle);
        //this.driverService.save(driver);


        return new ResponseEntity<>(VehicleMockup.getVehicleDTO(), HttpStatus.OK);

    }


    /**
     *
     *  GET MAPPINGS
     *
     * **/


    @GetMapping
    public ResponseEntity<DriverPaginated> getDrivers(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ){
        DriverPaginated driverPaginated = new DriverPaginated(243);
        driverPaginated.addDriver(DriverMockup.getDriver());
        return new ResponseEntity<>(driverPaginated, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Long id){

        //Driver driver = this.driverService.findOne(id);
        //if(driver == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(DriverMockup.getDriver(), HttpStatus.OK);
    }

            /**
             *
             *  GET DOCUMENTS
             *
             * **/

    @GetMapping(value = "/{id}/documents")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable("id") Long id){
        //Driver driver = this.driverService.findOne(id);
        //if(driver != null) return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(DocumentMockup.getDocumentDTO(), HttpStatus.OK);

    }

            /*
            *
            *  GET VEHICLE
            *
            * **/
    @GetMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable("id") Long id){
        //Vehicle vehicle = this.vehicleService.findOne(id);
        //if(vehicle != null) return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(VehicleMockup.getVehicleDTO(), HttpStatus.OK);
    }

    /**
     *
     *  DELETE MAPPINGS
     *
     * **/

    @DeleteMapping(value = "/{id}/documents")
    public ResponseEntity<Void> deleteDocuments(@PathVariable("id") Long id){
        //Driver driver = this.driverService.findOne(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @PostMapping(value = "/{id}/working-hours")
    public ResponseEntity<WorkingHoursDTO> createWorkingHours(@PathVariable("id") Long driverId){

       /* Driver driver = this.driverService.findOne(driverId);
        if(driver == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        WorkingHours workingHours = new WorkingHours();
        workingHours.setStartTime(Date.from(Instant.now()));
        workingHours.setEndTime(Date.from(Instant.now()));
        workingHours.setDriver(driver);

        workingHours = this.workingHoursService.save(workingHours);*/
        WorkingHoursDTO workingHours = new WorkingHoursDTO();
        workingHours.setId(Long.parseLong("10"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = null;
        try {
            date = sdf.parse("10-10-2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        workingHours.setStart(date);
        workingHours.setEnd(date);

        return new ResponseEntity<>(workingHours, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/working-hours")
    public ResponseEntity<WorkingHoursPaginated> getWorkingHours(
            @PathVariable("id") Long driverId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("from") String from,
            @RequestParam("to") String to
    )
    {
        WorkingHoursPaginated workingHoursPaginated = new WorkingHoursPaginated(243);
        WorkingHoursDTO workingHoursDTO = new WorkingHoursDTO();
        workingHoursDTO.setId(Long.parseLong("10"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = null;
        try {
            date = sdf.parse("10-10-2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        workingHoursDTO.setStart(date);
        workingHoursDTO.setEnd(date);
        workingHoursPaginated.addWorkingHours(workingHoursDTO);

        return new ResponseEntity<>(workingHoursPaginated, HttpStatus.OK);
    }


    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<DriverRidePaginated> getRides(
            @PathVariable("id") Long driverId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("from") String from,
            @RequestParam("to") String to){

        //TODO ERROR 500

        DriverRidePaginated driverRidePaginated = new DriverRidePaginated(243);
        DriverRideMockup ride = new DriverRideMockup();
        driverRidePaginated.addDriverRide(ride);

        return new ResponseEntity<>(driverRidePaginated, HttpStatus.OK);
    }

    @GetMapping(value = "/{driver-id}/working-hour/{working-hour-id}")
    public ResponseEntity<WorkingHoursDTO> getDetailsAboutWorkingHours(
            @PathVariable("driver-id") Long driverId,
            @PathVariable("working-hour-id") Long workingHourId)
    {
        WorkingHoursDTO workingHoursDTO = new WorkingHoursDTO();
        workingHoursDTO.setId(Long.parseLong("10"));
        workingHoursDTO.setStart(Date.from(Instant.now()));
        workingHoursDTO.setEnd(Date.from(Instant.now()));
        return new ResponseEntity<>(workingHoursDTO, HttpStatus.OK);

    }


    @PutMapping(value = "/{driver-id}/working-hour/{working-hour-id}")
    public ResponseEntity<WorkingHoursDTO> changeWorkingHours(
            @PathVariable("driver-id") Long driverId,
            @PathVariable("working-hour-id") Long workingHourId
    ){
        WorkingHoursDTO workingHoursDTO = new WorkingHoursDTO();
        workingHoursDTO.setId(Long.parseLong("10"));
        workingHoursDTO.setStart(Date.from(Instant.now()));
        workingHoursDTO.setEnd(Date.from(Instant.now()));

        return new ResponseEntity<>(workingHoursDTO, HttpStatus.OK);
    }







}
