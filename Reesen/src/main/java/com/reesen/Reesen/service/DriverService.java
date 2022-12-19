package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.CreatedDriverDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.repository.DriverRepository;
import com.reesen.Reesen.service.interfaces.IDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService implements IDriverService {
    private final DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository){
        this.driverRepository = driverRepository;
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
        driver.setPassword(driverDTO.getPassword());
        driver.setId(Long.parseLong("123"));
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

    public Driver getDriverFromDriverDTO(Long id, DriverDTO driverDTO){
        Driver driver = new Driver();
        driver.setEmail(driverDTO.getEmail());
        driver.setName(driverDTO.getName());
        driver.setSurname(driverDTO.getSurname());
        driver.setProfilePicture(driverDTO.getProfilePicture());
        driver.setTelephoneNumber(driverDTO.getTelephoneNumber());
        driver.setAddress(driverDTO.getAddress());
        driver.setId(id);
        driver.setPassword(driverDTO.getPassword());
        return driver;
    }

    @Override
    public Vehicle getVehicle(Long driverId){
        return this.driverRepository.getVehicle(driverId);
    }

    @Override
    public Page<Driver> findAll(Pageable page){
        return this.driverRepository.findAll(page);
    }
}
