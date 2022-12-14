package com.reesen.Reesen.repository;

import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    public VehicleType findByName(VehicleName name);
}
