package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.repository.DriverRepository;
import com.reesen.Reesen.service.interfaces.IDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Driver findOne(Long id) {
        return this.driverRepository.findById(id).orElseGet(null);
    }
}
