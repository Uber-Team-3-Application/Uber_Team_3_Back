package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.model.Vehicle;

import java.util.Optional;

public interface IVehicleService {

    Optional<Vehicle> findOne(Long id);
    Vehicle save(Vehicle vehicle);

    Vehicle updateLocation(Long vehicleId, LocationDTO locationDTO);
}
