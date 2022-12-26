package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    public Driver findByEmail(String email);
    public Driver findByEmailAndId(String email, Long id);

    @Query("select d.vehicle from Driver d where d.id=:driverId")
    Vehicle getVehicle(Long driverId);

    @Query("select d.password from Driver d where d.id=:id")
    String getPasswordWithId(Long id);
}
