package com.reesen.Reesen.service;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import java.time.LocalDateTime;

@Service
public class RideService implements IRideService {
	
    private final RideRepository rideRepository;
	private final RouteRepository routeRepository;
	private final PassengerRepository passengerRepository;
	private final VehicleTypeRepository vehicleTypeRepository;
	private final PanicRepository panicRepository;
	private final DriverRepository driverRepository;

    @Autowired
    public RideService(RideRepository rideRepository, RouteRepository routeRepository, PassengerRepository passengerRepository, VehicleTypeRepository vehicleTypeRepository, PanicRepository panicRepository, DriverRepository driverRepository){
        this.rideRepository = rideRepository;
		this.routeRepository = routeRepository;
		this.passengerRepository = passengerRepository;
		this.vehicleTypeRepository = vehicleTypeRepository;
		this.panicRepository = panicRepository;
		this.driverRepository = driverRepository;
	}

	@Override
	public Optional<Ride> findOne(Long id) {
		return this.rideRepository.findById(id);
	}

	@Override
	public Ride save(Ride ride) {
		return this.rideRepository.save(ride);
	}

	@Override
	public RideDTO createRideDTO(CreateRideDTO rideDTO) {
		Ride ride = new Ride();
		ride.setId(Long.parseLong("546"));
		Set<RouteDTO> locationsDTOs = rideDTO.getLocations();
		Set<Route> locations = new HashSet<>();
		for(RouteDTO routeDTO: locationsDTOs){
			locations.add(this.routeRepository.findById(routeDTO.getId()).get());
		}
		ride.setLocations(locations);
		ride.setVehicleType(this.vehicleTypeRepository.findByName(VehicleName.valueOf(rideDTO.getVehicleType())));
		ride.setBabyAccessible(rideDTO.isBabyTransport());
		ride.setPetAccessible(rideDTO.isPetTransport());
		Set<UserDTO> passengersDTOs = rideDTO.getPassengers();
		Set<Passenger> passengers = new HashSet<>();
		for(UserDTO userDTO: passengersDTOs){
			passengers.add(this.passengerRepository.findByEmail(userDTO.getEmail()));
		}
		ride.setPassengers(passengers);
		return new RideDTO(this.rideRepository.save(ride));
	}

	@Override
	public Optional<Ride> findDriverActiveRide(Long driverId) {
		return this.rideRepository.findRideByDriverIdAndStatus(driverId, RideStatus.ACTIVE);
	}

	@Override
	public Ride withdrawRide(Ride ride) {
		ride.setStatus(RideStatus.WITHDRAWN);
		return ride;
	}

	@Override
	public Ride panicRide(Ride ride, String reason) {
		this.panicRepository.save(new Panic(LocalDateTime.now(), reason, ride, ride.getDriver()));
		ride.setStatus(RideStatus.FINISHED);
		return ride;
	}

	@Override
	public Ride cancelRide(Ride ride, String reason) {
		ride.setStatus(RideStatus.REJECTED);
		Deduction deduction = new Deduction(ride, ride.getDriver(), reason, LocalDateTime.now());
		ride.setDeduction(deduction);
		return ride;
	}

	@Override
	public Ride endRide(Ride ride) {
		ride.setStatus(RideStatus.FINISHED);
		return ride;
	}

	@Override
	public Ride acceptRide(Ride ride) {
		ride.setStatus(RideStatus.ACCEPTED);
		return ride;
	}

	@Override
	public Page<Ride> findAll(Pageable page) {
		List<Ride> rides = this.rideRepository.findAll(page).stream().toList();
		for(Ride ride: rides){
			ride.setDriver(this.driverRepository.findDriverWhereRideEquals(ride));
		}
	}

	public Page<Ride> findAll(Long driverId, Pageable page, Date from, Date to){
		if(from == null && to == null)
			return this.rideRepository.findAllByDriverId(driverId, page);
		if(to != null && from == null)
			return this.rideRepository.findAllByDriverIdAndTimeOfEndBefore(driverId, to, page);
		if(to == null)
			return this.rideRepository.findAllByDriverIdAndTimeOfStartAfter(driverId, from, page);

		return this.rideRepository.findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(driverId,
				from,
				to,
				page);

	}
	@Override
	public Ride findPassengerActiveRide(Long passengerId) {
		Passenger passenger = this.passengerRepository.findById(passengerId).get();
		for(Ride ride: passenger.getRides()){
			if(ride.getStatus() == RideStatus.ACTIVE)
				return ride;
		}
		return null;
	}

}
