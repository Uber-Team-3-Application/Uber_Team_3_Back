package com.reesen.Reesen.repository;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.VehicleLocationWithAvailabilityDTO;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("select v.currentLocation from Vehicle v where v.id=:vehicleId")
    Location getLocation(Long vehicleId);

    @Query("select v.currentLocation.id from Vehicle v")
    List<Long> getAllLocationIds();

    @Query("select new com.reesen.Reesen.dto.VehicleLocationWithAvailabilityDTO(dr.isActive, v.currentLocation.address, v.currentLocation.latitude, v.currentLocation.longitude) from Vehicle v inner join v.driver dr where dr.vehicle.id = v.id and dr.isActive = true")
    List<VehicleLocationWithAvailabilityDTO> getAllLocationsWithAvailability();

    @Query("select v.type from Vehicle v where v.id=:id")
    VehicleType getVehicleType(Long id);
}
