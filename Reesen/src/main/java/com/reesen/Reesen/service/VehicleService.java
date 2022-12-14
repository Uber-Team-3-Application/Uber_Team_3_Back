package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.VehicleDTO;
import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.VehicleType;
import com.reesen.Reesen.repository.LocationRepository;
import com.reesen.Reesen.repository.VehicleRepository;
import com.reesen.Reesen.repository.VehicleTypeRepository;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, VehicleTypeRepository vehicleTypeRepository, LocationRepository locationRepository){
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public Optional<Vehicle> findOne(Long id) {
        return this.vehicleRepository.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return this.vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle updateLocation(Long vehicleId, LocationDTO locationDTO) {
        return this.vehicleRepository.updateLocation(vehicleId, locationDTO);
    }

    public Vehicle createVehicle(VehicleDTO vehicleDTO, Location location){
        Vehicle vehicle = new Vehicle();
        VehicleName vehicleName = VehicleName.getVehicleName(vehicleDTO.getVehicleType());

        vehicle.setType(this.findVehicleTypeByName(vehicleName));
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setRegistrationPlate(vehicleDTO.getLicenseNumber());
        vehicle.setCurrentLocation(location);
        vehicle.setBabyAccessible(vehicleDTO.isBabyTransport());
        vehicle.setPetAccessible(vehicleDTO.isPetTransport());
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());
        return this.vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle editVehicle(Vehicle vehicle, VehicleDTO vehicleDTO){
        vehicle.setType(this.findVehicleTypeByName(VehicleName.getVehicleName(vehicleDTO.getVehicleType())));
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPetAccessible(vehicle.isPetAccessible());
        vehicle.setBabyAccessible(vehicleDTO.isBabyTransport());
        vehicle.setRegistrationPlate(vehicle.getRegistrationPlate());
        Location location = vehicleRepository.getLocation(vehicle.getId());
        location.setAddress(vehicleDTO.getCurrentLocation().getAddress());
        location.setLatitude(vehicleDTO.getCurrentLocation().getLatitude());
        location.setLongitude(vehicleDTO.getCurrentLocation().getLongitude());
        location = this.locationRepository.save(location);
        vehicle.setCurrentLocation(location);
        return vehicle;

    }

    public VehicleType findVehicleTypeByName(VehicleName name){
        return this.vehicleTypeRepository.findByName(name);
    }

    @Override
    public Optional<VehicleType> findType(Long id){
        return this.vehicleTypeRepository.findById(id);
    }

    @Override
    public Location findLocation(Long id){
        return this.vehicleRepository.getLocation(id);
    }

    @Override

    public Vehicle createVehicle(VehicleDTO vehicleDTO, Driver driver){
        Location location = new Location();
        location.setLongitude(vehicleDTO.getCurrentLocation().getLongitude());
        location.setLatitude(vehicleDTO.getCurrentLocation().getLatitude());
        location.setAddress(vehicleDTO.getCurrentLocation().getAddress());
        location = this.locationRepository.save(location);
        Vehicle vehicle = new Vehicle();
        vehicle.setType(this.findVehicleTypeByName(VehicleName.getVehicleName(vehicleDTO.getVehicleType())));
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPetAccessible(vehicleDTO.isPetTransport());
        vehicle.setBabyAccessible(vehicleDTO.isBabyTransport());
        vehicle.setRegistrationPlate(vehicleDTO.getLicenseNumber());
        vehicle.setCurrentLocation(location);
        vehicle.setDriver(driver);

        return vehicle;
    }

}
