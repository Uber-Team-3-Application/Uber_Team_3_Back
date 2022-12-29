package com.reesen.Reesen.repository;

import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    @Query("select t from VehicleType t where t.name=:name")
    VehicleType findByName(VehicleName name);
}
