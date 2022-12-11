package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.VehicleDTO;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.VehicleType;

import java.util.Optional;

public interface IVehicleService {

    Vehicle findOne(Long id);
    Vehicle save(Vehicle vehicle);

    Vehicle createVehicle(VehicleDTO vehicleDTO, Location location);

    VehicleType findVehicleTypeByName(VehicleName name);

    Optional<VehicleType> findType(Long id);
    Location findLocation(Long id);
}
