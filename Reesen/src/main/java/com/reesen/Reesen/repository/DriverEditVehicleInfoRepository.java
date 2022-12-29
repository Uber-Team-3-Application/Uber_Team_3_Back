package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Driver.DriverEditVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverEditVehicleInfoRepository extends JpaRepository<DriverEditVehicle, Long> {

    @Query("select COUNT(u) from DriverEditVehicle u")
    int countTotal();
}
