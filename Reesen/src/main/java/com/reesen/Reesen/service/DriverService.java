package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {
    private final DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository){
        this.driverRepository = driverRepository;
    }

    public Driver add(Driver driver) {
        return this.driverRepository.save(driver);
    }

    public Driver update(Driver driver) {
        return this.driverRepository.save(driver);
    }

    public Driver findOne(Integer id) {
        return this.driverRepository.findById(id).orElseGet(null);
    }
}