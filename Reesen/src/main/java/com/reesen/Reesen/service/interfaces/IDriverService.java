package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.CreatedDriverDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.model.Driver;

public interface IDriverService {
    Driver save(Driver driver);
    Driver findOne(Long id);


    CreatedDriverDTO createDriverDTO(DriverDTO driverDTO);
}
