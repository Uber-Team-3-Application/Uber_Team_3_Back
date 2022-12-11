package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Vehicle;

public interface IVehicleService {

    Vehicle findOne(Long id);
    Vehicle save(Vehicle vehicle);
}
