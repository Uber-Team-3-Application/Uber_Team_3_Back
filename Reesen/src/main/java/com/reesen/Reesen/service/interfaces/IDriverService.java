package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.CreatedDriverDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.paginated.Paginated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IDriverService {
    Driver save(Driver driver);
    Optional<Driver> findOne(Long id);

    Page<Driver> findAll(Pageable page);

    CreatedDriverDTO createDriverDTO(DriverDTO driverDTO);
    Paginated<CreatedDriverDTO> getDriverPaginated();

    Driver findByEmail(String email);

    Driver findByEmailAndId(String email, Long id);
    Driver getDriverFromDriverDTO(Long id, DriverDTO driverDTO);

    Vehicle getVehicle(Long driverId);

}
