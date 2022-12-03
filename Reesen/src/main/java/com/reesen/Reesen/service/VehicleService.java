package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle findOne(Long id) {
        return this.vehicleRepository.findById(id).orElseGet(null);
    }

    public Vehicle save(Vehicle vehicle) {
        return this.vehicleRepository.save(vehicle);
    }


}
