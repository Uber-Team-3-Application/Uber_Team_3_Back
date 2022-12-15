package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.VehicleDTO;
import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.VehicleType;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

public interface IVehicleService {

    Optional<Vehicle> findOne(Long id);
    Vehicle save(Vehicle vehicle);
    Vehicle createVehicle(VehicleDTO vehicleDTO, Driver driver);
    Vehicle createVehicle(VehicleDTO vehicleDTO, Location location);
    Vehicle editVehicle(Vehicle vehicle, VehicleDTO vehicleDTO);

    VehicleType findVehicleTypeByName(VehicleName name);

    Optional<VehicleType> findType(Long id);
    Location findLocation(Long id);

    Vehicle setCurrentLocation(Vehicle vehicle, LocationDTO locationDTO);

    List<VehicleType> getVehicleTypes();
}
