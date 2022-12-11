package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.repository.VehicleRepository;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle findOne(Long id) {
        return this.vehicleRepository.findById(id).orElseGet(null);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return this.vehicleRepository.save(vehicle);
    }


}
