package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.CreatedDriverDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.paginated.Paginated;

import java.awt.print.Pageable;
import java.util.Optional;

public interface IDriverService {
    Driver save(Driver driver);
    Optional<Driver> findOne(Long id);


    CreatedDriverDTO createDriverDTO(DriverDTO driverDTO);
    Paginated<CreatedDriverDTO> getDriverPaginated();

    Driver findByEmail(String email);

    Driver findByEmailAndId(String email, Long id);
    Driver getDriverFromDriverDTO(Long id, DriverDTO driverDTO);

    Vehicle getVehicle(Long driverId);
}
