package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver findByEmail(String email);
    Driver findByEmailAndId(String email, Long id);

    @Query("select d.vehicle from Driver d where d.id=:driverId")
    Vehicle getVehicle(Long driverId);

    @Query("select d.password from Driver d where d.id=:id")
    String getPasswordWithId(Long id);

    Optional<Driver> findDriverByRidesContaining(Ride ride);
}
