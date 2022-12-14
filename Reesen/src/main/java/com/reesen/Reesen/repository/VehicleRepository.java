package com.reesen.Reesen.repository;

import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle updateLocation(Long vehicleId, LocationDTO locationDTO);
}
