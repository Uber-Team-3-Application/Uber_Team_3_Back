package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Driver.DriverEditBasicInformation;
import com.reesen.Reesen.model.Driver.DriverEditVehicle;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.security.jwt.JwtTokenUtil;
import com.reesen.Reesen.service.interfaces.*;
import com.reesen.Reesen.validation.UserRequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/driver")
public class DriverController {
    private final IDriverService driverService;
    private final IDocumentService documentService;
    private final IVehicleService vehicleService;
    private final IWorkingHoursService workingHoursService;
    private final ILocationService locationService;
    private final IRideService rideService;
    private final IPassengerService passengerService;
    private final IDeductionService deductionService;
    private final IRouteService routeService;

    @Autowired
    public DriverController(IDriverService driverService,
                            IDocumentService documentService,
                            IVehicleService vehicleService,
                            IWorkingHoursService workingHoursService,
                            ILocationService locationService,
                            IRideService rideService,
                            IPassengerService passengerService,
                            IDeductionService deductionService,
                            IRouteService routeService) {
        this.driverService = driverService;
        this.documentService = documentService;
        this.vehicleService = vehicleService;
        this.workingHoursService = workingHoursService;
        this.locationService = locationService;
        this.rideService = rideService;
        this.passengerService = passengerService;
        this.deductionService = deductionService;
        this.routeService = routeService;
    }


    /**
     * PUT MAPPINGS
     **/
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<CreatedDriverDTO> updateDriver(@Valid @RequestBody DriverDTO driverDTO, @PathVariable Long id) {

        if (this.driverService.findOne(id).isEmpty())
            return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        Driver driver = this.driverService.findByEmail(driverDTO.getEmail());

        if (driver != null && !driver.getId().toString().equals(id.toString())) {
            return new ResponseEntity("Invalid data. For example bad email format.", HttpStatus.BAD_REQUEST);
        }
        driver = this.driverService.getDriverFromDriverDTO(id, driverDTO);

        this.driverService.saveEditBasicInfo(driver, id);
        CreatedDriverDTO updatedDriver = new CreatedDriverDTO(driver);
        return new ResponseEntity<>(updatedDriver, HttpStatus.OK);


    }

    @PutMapping(value = "/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreatedDriverDTO> updateDriverAsAdmin(@Valid @RequestBody DriverDTO driverDTO, @PathVariable Long id) {

        if (this.driverService.findOne(id).isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Driver driver = this.driverService.findByEmail(driverDTO.getEmail());
        if (driver != null && !driver.getId().toString().equals(id.toString())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        driver = this.driverService.getDriverFromDriverDTO(id, driverDTO);
        this.driverService.save(driver);
        CreatedDriverDTO updatedDriver = new CreatedDriverDTO(driver);
        return new ResponseEntity<>(updatedDriver, HttpStatus.OK);


    }

    /**
     * PUT Vehicle
     **/
    @PutMapping(value = "/{id}/vehicle")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<VehicleDTO> updateVehicle(@Valid @RequestBody VehicleDTO vehicleDTO, @PathVariable("id") Long driverId) {

        if (driverId < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Driver> driver = this.driverService.findOne(driverId);
        if (driver.isEmpty()) return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        Vehicle vehicle = this.driverService.getVehicle(driverId);
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        vehicle = this.vehicleService.createVehicle(vehicleDTO, driver.get());
        this.driverService.saveEditVehicle(vehicle, driverId);
        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }


    @PutMapping(value = "/{id}/vehicle-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VehicleDTO> updateVehicleAsAdmin(@Valid @RequestBody VehicleDTO vehicleDTO, @PathVariable("id") Long driverId) {

        if (driverId < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Driver> driver = this.driverService.findOne(driverId);
        if (driver.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Vehicle vehicle = this.driverService.getVehicle(driverId);
        // if ! exists -> create
        if (vehicle == null) {
            vehicle = this.vehicleService.createVehicle(vehicleDTO, driver.get());
        } else {
            // if exists -> edit
            vehicle = this.vehicleService.editVehicle(vehicle, vehicleDTO);
        }
        this.vehicleService.save(vehicle);
        driver.get().setVehicle(vehicle);
        this.driverService.save(driver.get());

        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }

    /**
     * PUT Working Hours
     **/

    @PutMapping(value = "/working-hour/{working-hour-id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<WorkingHoursDTO> changeWorkingHours(
            @Valid @RequestBody ChangeWorkingHoursDTO workingHoursDTO,
            @PathVariable("working-hour-id") Long workingHourId
    ) {

        Optional<WorkingHours> workingHours = this.workingHoursService.findOne(workingHourId);
        if (workingHours.isEmpty()) return new ResponseEntity("Working hour does not exist", HttpStatus.NOT_FOUND);
        String workingHoursValid = this.workingHoursService.validateWorkingHours(workingHours.get(), workingHoursDTO);
        if(!workingHoursValid.equalsIgnoreCase("valid")){
            return new ResponseEntity(workingHoursValid, HttpStatus.BAD_REQUEST);
        }
        WorkingHours updatedWH = this.workingHoursService.editWorkingHours(workingHours.get(), workingHoursDTO);
        this.workingHoursService.save(updatedWH);

        return new ResponseEntity<>(new WorkingHoursDTO(updatedWH), HttpStatus.OK);
    }

    /**
     * POST MAPPINGS
     **/
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreatedDriverDTO> createDriver(@Valid @RequestBody DriverDTO driverDTO) {

        if (this.driverService.findByEmail(driverDTO.getEmail()) != null)
            return new ResponseEntity("User with that email already exists!", HttpStatus.BAD_REQUEST);

        CreatedDriverDTO createdDriverDTO = this.driverService.createDriverDTO(driverDTO);
        return new ResponseEntity<>(createdDriverDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/activity")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<String> changeDriverActivity(@PathVariable("id") Long driverId,
                                                       @Valid @RequestBody DriverActivityDTO driverActivityDTO) {

        boolean isActive = driverActivityDTO.isActive();
        Optional<Driver> driver = this.driverService.findOne(driverId);
        if (driver.isPresent()) {
            driver.get().setActive(isActive);
            this.driverService.save(driver.get());
        }
        return new ResponseEntity<>("Driver Activity updated!", HttpStatus.OK);
    }

    /**
     * POST Documents
     **/

    @PostMapping(value = "/{id}/documents")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    public ResponseEntity<DocumentDTO> addDocument(
            @Valid @RequestBody DocumentDTO documentDTO,
            @PathVariable("id") Long driverId,
            @RequestHeader Map<String, String> headers

    ) {

        String role = UserRequestValidation.getRoleFromToken(headers);
        if(role.equalsIgnoreCase("driver")){
            boolean areIdsEqual = UserRequestValidation.areIdsEqual(headers, driverId);
            if(!areIdsEqual) return new ResponseEntity("Driver does not exist.", HttpStatus.NOT_FOUND);
        }

        Optional<Driver> driver = this.driverService.findOne(driverId);
        if (driver.isEmpty()) return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        Document document = new Document(documentDTO.getName(), documentDTO.getDocumentImage(), driver.get());
        document = this.documentService.save(document);

        return new ResponseEntity<>(new DocumentDTO(document), HttpStatus.OK);

    }

    /**
     * POST Vehicles
     **/


    @PostMapping(value = "/{id}/vehicle")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<VehicleDTO> addVehicle(@Valid @RequestBody VehicleDTO vehicleDTO, @PathVariable("id") Long driverId) {

        if (driverId < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Driver> driver = this.driverService.findOne(driverId);
        if (driver.isEmpty()) return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        Location location = this.locationService.getLocation(vehicleDTO.getCurrentLocation());
        Vehicle vehicle = this.vehicleService.createVehicle(vehicleDTO, location);
        vehicle.setDriver(driver.get());

        driver.get().setVehicle(vehicle);
        this.driverService.save(driver.get());


        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }


    /**
     * POST Working Hours
     **/
    @PostMapping(value = "/{id}/working-hour")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    public ResponseEntity<WorkingHoursDTO> createWorkingHours(@Valid @RequestBody CreateWorkingHoursDTO workingHoursDTO, @PathVariable("id") Long driverId) {

        if (driverId < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Driver> driver = this.driverService.findOne(driverId);
        if (driver.isEmpty()) return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        WorkingHours workingHours = this.workingHoursService.createWorkingHours(workingHoursDTO, driver.get());
        workingHours = this.workingHoursService.save(workingHours);

        return new ResponseEntity<>(new WorkingHoursDTO(workingHours), HttpStatus.OK);
    }

    /**
     * GET MAPPINGS
     **/

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paginated<CreatedDriverDTO>> getDrivers(
            Pageable page
    ) {
        Page<Driver> drivers = this.driverService.findAll(page);
        Set<CreatedDriverDTO> driverDTOS = new HashSet<>();
        for (Driver driver : drivers) {
            driverDTOS.add(new CreatedDriverDTO(driver));
        }
        return new ResponseEntity<>(
                new Paginated<>(drivers.getNumberOfElements(), driverDTOS), HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER', 'ADMIN')")
    public ResponseEntity<CreatedDriverDTO> getDriver(@PathVariable Long id) {

        Optional<Driver> driver = this.driverService.findOne(id);
        if (driver.isEmpty()) return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        CreatedDriverDTO driverDTO = new CreatedDriverDTO(driver.get());

        return new ResponseEntity<>(driverDTO, HttpStatus.OK);

    }

    /**
     * GET DOCUMENTS
     **/

    @GetMapping(value = "/{id}/documents")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Set<DocumentDTO>> getDocument(@PathVariable("id") Long id) {

        if (id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Driver> driver = this.driverService.findOne(id);
        if (driver.isEmpty()) return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        List<Document> documents = this.documentService.findAllByDriverId(driver.get().getId());

        return new ResponseEntity<>(this.documentService.getDocumentDTOS(documents), HttpStatus.OK);

    }

    /*
     *
     *  GET VEHICLE
     *
     * **/
    @GetMapping(value = "/{id}/vehicle")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable("id") Long id) {
        if (id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (this.driverService.findOne(id).isEmpty())
            return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        Vehicle vehicle = this.driverService.getVehicle(id);
        if (vehicle == null) return new ResponseEntity("Vehicle is not assigned!", HttpStatus.BAD_REQUEST);

        VehicleType type = this.vehicleService.findType(vehicle.getId());
        Location location = this.vehicleService.findLocation(vehicle.getId());
        vehicle.setType(type);
        vehicle.setCurrentLocation(location);

        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }
    /*
     *
     *  GET Working Hours
     *
     * **/

    @GetMapping(value = "/{id}/working-hour")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Paginated<WorkingHoursDTO>> getWorkingHours(
            Pageable page,
            @PathVariable("id") Long driverId,
            @RequestParam("from") LocalDateTime from,
            @RequestParam("to") LocalDateTime to
    ) {
        Optional<Driver> driver = this.driverService.findOne(driverId);
        if (driver.isEmpty()) return new ResponseEntity("Driver does not exist", HttpStatus.NOT_FOUND);

        Page<WorkingHours> workingHours;
        workingHours = this.workingHoursService.findAll(driverId, page, from, to);

        Set<WorkingHoursDTO> workingHoursDTOS = new HashSet<>();
        for (WorkingHours workingHour : workingHours) {
            workingHoursDTOS.add(new WorkingHoursDTO(workingHour));
        }
        return new ResponseEntity<>(new Paginated<>
                (workingHours.getNumberOfElements(), workingHoursDTOS),
                HttpStatus.OK);
    }

    @GetMapping(value = "/working-hour/{working-hour-id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<WorkingHoursDTO> getDetailsAboutWorkingHours(
            @PathVariable("working-hour-id") Long workingHourId) {

        if (workingHourId < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<WorkingHours> workingHours = this.workingHoursService.findOne(workingHourId);
        if (workingHours.isEmpty()) return new ResponseEntity("Working hour does not exist!", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new WorkingHoursDTO(workingHours.get()), HttpStatus.OK);
    }

    /*
     *
     *  GET Rides
     *
     * **/
    @GetMapping(value = "/{id}/ride")
//    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Paginated<DriverRideDTO>> getRides(
            @PathVariable("id") Long driverId,
            Pageable page,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to)


    {
        Optional<Driver> driver = this.driverService.findOne(driverId);

        if (driver.isEmpty()) return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        Date dateFrom = null;
        Date dateTo = null;

        if (from != null || to != null) {
            if  (from != null) {
                LocalDate date = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
                dateFrom = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            if (to != null) {
                LocalDate date = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);
                dateTo = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        }

        Page<Ride> driversRides = this.rideService.findAll(driverId, page, dateFrom, dateTo);

        LinkedHashSet<DriverRideDTO> rideDTOs = new LinkedHashSet<>();


        for (Ride ride : driversRides) {
            ride.setDriver(driver.get());
            ride.setPassengers(passengerService.findPassengersByRidesContaining(ride));
            ride.setDeduction(deductionService.findDeductionByRide(ride).orElse(new Deduction()));
            Set<Route> locations;
            locations = rideService.getLocationsByRide(ride.getId());
            for (Route location : locations) {
                location.setDestination(this.routeService.getDestinationByRoute(location).get());
                location.setDeparture(this.routeService.getDepartureByRoute(location).get());

            }

            ride.setLocations(locations);
            rideDTOs.add(new DriverRideDTO(ride));
        }

        return new ResponseEntity<>(new Paginated<>(driversRides.getNumberOfElements(), rideDTOs), HttpStatus.OK);
    }

    /**
     * DELETE MAPPINGS
     **/
    @DeleteMapping(value = "/document/{document-id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<String> deleteDocuments(@PathVariable("document-id") Long id) {

        if (id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (this.documentService.findOne(id).isEmpty())
            return new ResponseEntity<>("Document does not exist!", HttpStatus.NOT_FOUND);

        this.documentService.delete(id);
        return new ResponseEntity<>("Driver document deleted successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/total-edit-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> getTotalEditRequests() {

        int totalEditRequests = this.driverService.getTotalEditRequests();
        return new ResponseEntity<>(totalEditRequests, HttpStatus.OK);
    }

    @GetMapping(value = "/profile-edit-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DriverEditBasicInformation>> getProfileEditRequests() {
        List<DriverEditBasicInformation> driverEditBasicInformation =
                this.driverService.getDriverEditBasicInfo();
        if (driverEditBasicInformation.size() == 0)
            return new ResponseEntity(new ArrayList<>(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(driverEditBasicInformation, HttpStatus.OK);

    }

    @GetMapping(value = "/vehicle-edit-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DriverEditVehicle>> getVehicleEditRequests() {
        List<DriverEditVehicle> driverEditVehicleInformation =
                this.driverService.getDriverEditVehicle();
        if (driverEditVehicleInformation.size() == 0)
            return new ResponseEntity(new ArrayList<>(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(driverEditVehicleInformation, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/accept-vehicle-edit-request")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> acceptVehicleEditRequest(@PathVariable("id") Long editRequestId) {
        Optional<DriverEditVehicle> driverEditBasicInformation =
                this.driverService.findOneEditVehicleRequest(editRequestId);

        if (driverEditBasicInformation.isEmpty()) return new ResponseEntity<>("Invalid id", HttpStatus.NOT_FOUND);

        Optional<Driver> driver = this.driverService.findOne(driverEditBasicInformation.get().getDriverId());
        if (driver.isEmpty()) return new ResponseEntity<>("Non existing driver", HttpStatus.BAD_REQUEST);

        Vehicle vehicle = this.driverService.updateVehicleBasedOnEditRequest(driver.get(), driverEditBasicInformation.get());
        this.vehicleService.save(vehicle);

        this.driverService.declineVehicleEditRequest(editRequestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/accept-profile-edit-request")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> acceptProfileEditRequest(@PathVariable("id") Long editRequestId) {


        Optional<DriverEditBasicInformation> driverEditBasicInformation =
                this.driverService.findOneEditProfileRequest(editRequestId);

        if (driverEditBasicInformation.isEmpty()) return new ResponseEntity<>("Invalid id", HttpStatus.NOT_FOUND);

        // find driver
        Optional<Driver> driver = this.driverService.findOne(driverEditBasicInformation.get().getDriverId());
        if (driver.isEmpty()) return new ResponseEntity<>("Non existing driver", HttpStatus.BAD_REQUEST);

        this.driverService.updateDriverBasedOnEditRequest(driver.get(), driverEditBasicInformation.get());
        this.driverService.declineProfileEditRequest(editRequestId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping(value = "/{id}/decline-vehicle-edit-request")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> declineVehicleEditRequest(@PathVariable("id") Long editRequestId) {

        Optional<DriverEditVehicle> driverEditBasicInformation =
                this.driverService.findOneEditVehicleRequest(editRequestId);

        if (driverEditBasicInformation.isEmpty()) return new ResponseEntity<>("Invalid id", HttpStatus.NOT_FOUND);

        this.driverService.declineVehicleEditRequest(editRequestId);

        return new ResponseEntity<>("Deleted request.", HttpStatus.OK);

    }

    @DeleteMapping(value = "/{id}/decline-profile-edit-request")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> declineProfileEditRequest(@PathVariable("id") Long editRequestId) {
        Optional<DriverEditBasicInformation> driverEditBasicInformation =
                this.driverService.findOneEditProfileRequest(editRequestId);

        if (driverEditBasicInformation.isEmpty()) return new ResponseEntity("Invalid id",
                HttpStatus.NOT_FOUND);
        this.driverService.declineProfileEditRequest(editRequestId);
        return new ResponseEntity<>("Deleted request.", HttpStatus.OK);

    }
}
