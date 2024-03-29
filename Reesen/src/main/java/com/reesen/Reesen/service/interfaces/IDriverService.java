package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.CreatedDriverDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.dto.DriverStatisticsDTO;
import com.reesen.Reesen.dto.UpdateDriverDTO;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Driver.DriverEditBasicInformation;
import com.reesen.Reesen.model.Driver.DriverEditVehicle;
import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.paginated.Paginated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IDriverService {
    Driver save(Driver driver);
    Optional<Driver> findOne(Long id);

    Page<Driver> findAll(Pageable page);

    CreatedDriverDTO createDriverDTO(DriverDTO driverDTO);
    Paginated<CreatedDriverDTO> getDriverPaginated();

    Driver findByEmail(String email);

    Driver findByEmailAndId(String email, Long id);
    Driver getDriverFromDriverDTO(Long id, UpdateDriverDTO driverDTO);

    Vehicle getVehicle(Long driverId);
    Driver findDriverByRidesContaining(Ride ride);

    int getTotalEditRequests();

    List<DriverEditVehicle> getDriverEditVehicle();

    List<DriverEditBasicInformation> getDriverEditBasicInfo();

    DriverEditVehicle saveEditVehicle(Vehicle vehicle, Long driverId);
    DriverEditBasicInformation saveEditBasicInfo(Driver driver, Long driverId);

    Optional<DriverEditVehicle> findOneEditVehicleRequest(Long editRequestId);

    Optional<DriverEditBasicInformation> findOneEditProfileRequest(Long editRequestId);

    void declineProfileEditRequest(Long editRequestId);

    void declineVehicleEditRequest(Long editRequestId);

    void updateDriverBasedOnEditRequest(Driver driver, DriverEditBasicInformation driverEditBasicInformation);

    Vehicle updateVehicleBasedOnEditRequest(Driver driver, DriverEditVehicle driverEditVehicle);

    Set<Ride> getDriverRides(Long id);


    Optional<Driver> findDriverWithRide(Ride ride);
    Set<Review> getAllReviews(Long driverId);

    Set<DriverStatisticsDTO> getStatistics(Long driverId);
}
