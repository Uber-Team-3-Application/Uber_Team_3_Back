package com.reesen.Reesen.controller;


import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.mockup.*;
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
import java.util.Set;

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
    public ResponseEntity<CreatedDriverDTO> updateDriver(@RequestBody DriverDTO driverDTO, @PathVariable Long id){

        CreatedDriverDTO driver = new CreatedDriverDTO();
        driver.setEmail(driverDTO.getEmail());
        driver.setName(driverDTO.getName());
        driver.setSurname(driverDTO.getSurname());
        driver.setProfilePicture(driverDTO.getProfilePicture());
        driver.setTelephoneNumber(driverDTO.getTelephoneNumber());
        driver.setAddress(driverDTO.getAddress());
        driver.setId(id);

        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

            /**
             *
             *  PUT Vehicle
             *
             * **/
    @PutMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> updateVehicle(@RequestBody VehicleDTO vehicleDTO, @PathVariable("id") Long driverId){
        return new ResponseEntity<>(VehicleMockup.getVehicleDTO(), HttpStatus.OK);
    }

            /**
             *
             *  PUT Working Hours
             *
             * **/

    @PutMapping(value = "/working-hour/{working-hour-id}")
    public ResponseEntity<WorkingHoursDTO> changeWorkingHours(
            @PathVariable("working-hour-id") Long workingHourId
    ){
        WorkingHoursDTO workingHoursDTO = new WorkingHoursDTO();
        workingHoursDTO.setId(workingHourId);
        workingHoursDTO.setStart(Date.from(Instant.now()));
        workingHoursDTO.setEnd(Date.from(Instant.now()));

        return new ResponseEntity<>(workingHoursDTO, HttpStatus.OK);
    }
    /**
     *
     *  POST MAPPINGS
     *
     * **/
    @PostMapping
    public ResponseEntity<CreatedDriverDTO> createDriver(@RequestBody DriverDTO driverDTO){

        CreatedDriverDTO createdDriverDTO = this.driverService.createDriverDTO(driverDTO);
        return new ResponseEntity<>(createdDriverDTO, HttpStatus.OK);
    }

            /**
             *
             *  POST Documents
             *
             * **/

    @PostMapping(value = "/{id}/documents")
    public ResponseEntity<DocumentDTO> addDocument(@RequestBody DocumentDTO documentDTO, @PathVariable("id") Long driverId){
        return new ResponseEntity<>(DocumentMockup.getDocumentDTO(), HttpStatus.OK);

    }

            /**
             *
             *  POST Vehicles
             *
             * **/


    @PostMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> addVehicle(@RequestBody VehicleDTO vehicleDTO, @PathVariable("id") Long driverId){
        return new ResponseEntity<>(VehicleMockup.getVehicleDTO(), HttpStatus.OK);
    }


    /**
     *
     *  GET MAPPINGS
     *
     * **/


    @GetMapping
    public ResponseEntity<Paginated<DriverDTO>> getDrivers(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ){

        Paginated<DriverDTO> driverPaginated = new Paginated<>(243);
        driverPaginated.add(DriverMockup.getDriver());
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
             *  POST Working Hours
             *
             * **/
    @PostMapping(value = "/{id}/working-hours")
    public ResponseEntity<WorkingHoursDTO> createWorkingHours(@PathVariable("id") Long driverId){

        WorkingHoursDTO workingHours = new WorkingHoursDTO();
        workingHours.setId(Long.parseLong("123"));
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
        DriverPaginated driverPaginated = this.driverService.getDriverPaginated();
        return new ResponseEntity<>(driverPaginated, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CreatedDriverDTO> getDriver(@PathVariable Long id){
        return new ResponseEntity<>(DriverMockup.getDriver(id), HttpStatus.OK);
    }

            /**
             *
             *  GET DOCUMENTS
             *
             * **/

    @GetMapping(value = "/{id}/documents")
    public ResponseEntity<Set<DocumentDTO>> getDocument(@PathVariable("id") Long id){
        return new ResponseEntity<>(DocumentMockup.getDocumentsDTO(id), HttpStatus.OK);

    }
            /*
            *
            *  GET VEHICLE
            *
            * **/
    @GetMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable("id") Long id){
        return new ResponseEntity<>(VehicleMockup.getVehicleDTO(), HttpStatus.OK);
    }
            /*
             *
             *  GET Working Hours
             *
             * **/

    @GetMapping(value = "/{id}/working-hours")
    public ResponseEntity<Paginated<WorkingHoursDTO>> getWorkingHours(
            @PathVariable("id") Long driverId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("from") String from,
            @RequestParam("to") String to
    )
    {
        Paginated<WorkingHoursDTO> workingHoursPaginated = new Paginated<WorkingHoursDTO>(243);
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
        workingHoursPaginated.add(workingHoursDTO);

        return new ResponseEntity<>(workingHoursPaginated, HttpStatus.OK);
    }
    @GetMapping(value = "/working-hour/{working-hour-id}")
    public ResponseEntity<WorkingHoursDTO> getDetailsAboutWorkingHours(
            @PathVariable("working-hour-id") Long workingHourId)
    {
        WorkingHoursDTO workingHoursDTO = new WorkingHoursDTO();
        workingHoursDTO.setId(workingHourId);
        workingHoursDTO.setStart(Date.from(Instant.now()));
        workingHoursDTO.setEnd(Date.from(Instant.now()));
        return new ResponseEntity<>(workingHoursDTO, HttpStatus.OK);
    }

    /*
     *
     *  GET Rides
     *
     * **/

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<Paginated<DriverRideMockup>> getRides(
            @PathVariable("id") Long driverId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("from") String from,
            @RequestParam("to") String to)
    {

        Paginated<DriverRideMockup> driverRidePaginated = new Paginated<DriverRideMockup>(243);
        DriverRideMockup ride = new DriverRideMockup(123);
        driverRidePaginated.add(ride);

        return new ResponseEntity<>(driverRidePaginated, HttpStatus.OK);
    }

    /**
     *
     *  DELETE MAPPINGS
     *
     * **/
    @DeleteMapping(value = "/{id}/documents")
    public ResponseEntity<String> deleteDocuments(@PathVariable("id") Long id){
        return new ResponseEntity<>("Driver deleted successfully", HttpStatus.NO_CONTENT);
    }

}
