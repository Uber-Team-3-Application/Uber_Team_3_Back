package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.CreatedDriverDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.model.paginated.Paginated;

import java.awt.print.Pageable;

public interface IDriverService {
    Driver save(Driver driver);
    Driver findOne(Long id);


    CreatedDriverDTO createDriverDTO(DriverDTO driverDTO);
    Paginated<CreatedDriverDTO> getDriverPaginated();
}
