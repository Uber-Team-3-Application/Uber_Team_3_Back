package com.reesen.Reesen.service;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.CreatedDriverDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.dto.UpdateDriverDTO;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Driver.DriverEditBasicInformation;
import com.reesen.Reesen.model.Driver.DriverEditVehicle;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.VehicleType;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.repository.DriverEditBasicInfoRepository;
import com.reesen.Reesen.repository.DriverEditVehicleInfoRepository;
import com.reesen.Reesen.repository.DriverRepository;
import com.reesen.Reesen.repository.VehicleTypeRepository;
import com.reesen.Reesen.service.interfaces.IDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService implements IDriverService {
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final DriverEditBasicInfoRepository driverEditBasicInfoRepository;
    private final DriverEditVehicleInfoRepository driverEditVehicleInfoRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository, PasswordEncoder passwordEncoder, DriverEditBasicInfoRepository driverEditBasicInfoRepository, DriverEditVehicleInfoRepository driverEditVehicleInfoRepository, VehicleTypeRepository vehicleTypeRepository){
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
        this.driverEditBasicInfoRepository = driverEditBasicInfoRepository;
        this.driverEditVehicleInfoRepository = driverEditVehicleInfoRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @Override
    public Driver save(Driver driver) {
        return this.driverRepository.save(driver);
    }

    @Override
    public Optional<Driver> findOne(Long id) {
        return this.driverRepository.findById(id);
    }

    @Override
    public Driver findByEmail(String email){
        return this.driverRepository.findByEmail(email);
    }

    @Override
    public Driver findByEmailAndId(String email, Long id) {
        return this.driverRepository.findByEmailAndId(email, id);
    }

    @Override
    public CreatedDriverDTO createDriverDTO(DriverDTO driverDTO) {
        Driver driver = new Driver();
        driver.setName(driverDTO.getName());
        driver.setSurname(driverDTO.getSurname());
        driver.setProfilePicture(driverDTO.getProfilePicture());
        driver.setTelephoneNumber(driverDTO.getTelephoneNumber());
        driver.setEmail(driverDTO.getEmail());
        driver.setAddress(driverDTO.getAddress());
        driver.setPassword(passwordEncoder.encode(driverDTO.getPassword()));
        driver.setId(Long.parseLong("123"));
        driver.setRole(Role.DRIVER);
        return new CreatedDriverDTO(this.driverRepository.save(driver));
    }

    @Override
    public Paginated<CreatedDriverDTO> getDriverPaginated() {
        Paginated<CreatedDriverDTO> driverPaginated = new Paginated<>(243);
        CreatedDriverDTO createdDriverDTO = new CreatedDriverDTO();
        createdDriverDTO.setId(Long.parseLong("123"));
        createdDriverDTO.setName("Pera");
        createdDriverDTO.setSurname("Peric");
        createdDriverDTO.setProfilePicture("U3dhZ2dlciByb2Nrcw==");
        createdDriverDTO.setTelephoneNumber("+381123123");
        createdDriverDTO.setEmail("pera.peric@email.com");
        createdDriverDTO.setAddress("Bulevar Oslobodjenja 74");
        driverPaginated.addResult(createdDriverDTO);
        return driverPaginated;
    }

    public Driver getDriverFromDriverDTO(Long id, UpdateDriverDTO driverDTO){
        Optional<Driver> optDriver = this.driverRepository.findById(id);
        Driver driver = new Driver();
        if(optDriver.isPresent()) driver = optDriver.get();
        driver.setEmail(driverDTO.getEmail());
        driver.setName(driverDTO.getName());
        driver.setSurname(driverDTO.getSurname());
        driver.setProfilePicture(driverDTO.getProfilePicture());
        driver.setTelephoneNumber(driverDTO.getTelephoneNumber());
        driver.setAddress(driverDTO.getAddress());
        driver.setId(id);

        driver.setRole(Role.DRIVER);

        return driver;
    }

    @Override
    public Vehicle getVehicle(Long driverId){
        return this.driverRepository.getVehicle(driverId);
    }

    @Override
    public Driver findDriverByRidesContaining(Ride ride) {
        return  this.driverRepository.findDriverByRidesContaining(ride).get();
    }

    @Override
    public int getTotalEditRequests() {
        return this.driverEditBasicInfoRepository.countTotal() + this.driverEditVehicleInfoRepository.countTotal();
    }



    @Override
    public List<DriverEditVehicle> getDriverEditVehicle() {
        return this.driverEditVehicleInfoRepository.findAll();
    }

    @Override
    public List<DriverEditBasicInformation> getDriverEditBasicInfo() {
        return this.driverEditBasicInfoRepository.findAll();
    }

    @Override
    public DriverEditVehicle saveEditVehicle(Vehicle vehicle, Long driverId) {
        DriverEditVehicle driverEditVehicle = new DriverEditVehicle(vehicle, driverId);
        return this.driverEditVehicleInfoRepository.save(driverEditVehicle);
    }

    @Override
    public DriverEditBasicInformation saveEditBasicInfo(Driver driver, Long driverId) {
        DriverEditBasicInformation driverEditBasicInformation = new DriverEditBasicInformation(driver, driverId);
        return this.driverEditBasicInfoRepository.save(driverEditBasicInformation);
    }

    @Override
    public Optional<DriverEditVehicle> findOneEditVehicleRequest(Long editRequestId) {
        return this.driverEditVehicleInfoRepository.findById(editRequestId);
    }

    @Override
    public Optional<DriverEditBasicInformation> findOneEditProfileRequest(Long editRequestId) {
        return this.driverEditBasicInfoRepository.findById(editRequestId);
    }

    @Override
    public void declineProfileEditRequest(Long editRequestId) {
        this.driverEditBasicInfoRepository.deleteById(editRequestId);
    }

    @Override
    public void declineVehicleEditRequest(Long editRequestId) {

        this.driverEditVehicleInfoRepository.deleteById(editRequestId);
    }

    @Override
    public void updateDriverBasedOnEditRequest(Driver driver, DriverEditBasicInformation driverEditBasicInformation) {
        driver.setName(driverEditBasicInformation.getName());
        driver.setSurname(driverEditBasicInformation.getSurname());
        driver.setProfilePicture(driverEditBasicInformation.getProfilePicture());
        driver.setTelephoneNumber(driverEditBasicInformation.getTelephoneNumber());
        driver.setEmail(driverEditBasicInformation.getEmail());
        driver.setAddress(driverEditBasicInformation.getAddress());
        this.driverRepository.save(driver);
    }

    @Override
    public Vehicle updateVehicleBasedOnEditRequest(Driver driver, DriverEditVehicle driverEditVehicle) {
        Vehicle vehicle = this.getVehicle(driverEditVehicle.getDriverId());
        vehicle.setModel(driverEditVehicle.getVModel());
        vehicle.setType(this.findVehicleTypeByName(VehicleName.getVehicleName(driverEditVehicle.getVtype())));
        vehicle.setRegistrationPlate(driverEditVehicle.getVRegistrationPlate());
        vehicle.setPassengerSeats(driverEditVehicle.getVNumberOfSeats());
        vehicle.setBabyAccessible(driverEditVehicle.isVIsBabyAccessible());
        vehicle.setPetAccessible(driverEditVehicle.isVIsPetAccessible());
        return vehicle;
    }

    @Override
    public Optional<Driver> findDriverWithRide(Ride ride) {
        return this.driverRepository.findDriverByRidesContaining(ride);
    }

    public VehicleType findVehicleTypeByName(VehicleName name){
        return this.vehicleTypeRepository.findByName(name);
    }

    @Override
    public Page<Driver> findAll(Pageable page){
        return this.driverRepository.findAll(page);
    }
}
