package com.reesen.Reesen.repository;
import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

   // Vehicle updateLocation(Long vehicleId, LocationDTO locationDTO);
    
    @Query("select v.currentLocation from Vehicle v where v.id=:vehicleId")
    Location getLocation(Long vehicleId);
}
