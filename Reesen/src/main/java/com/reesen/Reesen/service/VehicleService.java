package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.repository.VehicleRepository;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> findOne(Long id) {
        return this.vehicleRepository.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return this.vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle updateLocation(Long vehicleId, LocationDTO locationDTO) {
        return this.vehicleRepository.updateLocation(vehicleId, locationDTO);
    }


}
