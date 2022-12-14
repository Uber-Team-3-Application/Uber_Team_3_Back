package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.model.VehicleType;
import com.reesen.Reesen.repository.PassengerRepository;
import com.reesen.Reesen.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.repository.RideRepository;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

@Service
public class RideService implements IRideService {
	
    private final RideRepository rideRepository;
	private final RouteRepository routeRepository;
	private final PassengerRepository passengerRepository;

    @Autowired
    public RideService(RideRepository rideRepository, RouteRepository routeRepository, PassengerRepository passengerRepository){
        this.rideRepository = rideRepository;
		this.routeRepository = routeRepository;
		this.passengerRepository = passengerRepository;
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
		//ride.setVehicleType(rideDTO.getVehicleType());
		ride.setBabyAccessible(rideDTO.isBabyTransport());
		ride.setPetAccessible(rideDTO.isPetTransport());
		Set<UserDTO> passengersDTOs = rideDTO.getPassengers();
		for(UserDTO userDTO: passengersDTOs){
			locations.add(this.passengerRepository.findByEmail(userDTO.getEmail()));
		}
		Set<Passenger> passengers = new HashSet<>();
		ride.setPassengers(passengers);
		return new RideDTO(this.rideRepository.save(ride));
	}

	@Override
	public Optional<Ride> findDriverActiveRide(Long driverId) {
		return this.rideRepository.findDriverActiveRide(driverId);
	}

	@Override
	public Ride withdrawRide(Long id) {
		return this.rideRepository.withdrawRide(id);
	}

	@Override
	public Ride panicRide(Long id, String reason) {
		return this.rideRepository.panicPressed(id, reason);
	}

	@Override
	public Ride cancelRide(Long id, String reason) {
		return this.rideRepository.cancelRide(id, reason);
	}

	@Override
	public Ride endRide(Long id) {
		return this.rideRepository.endRide(id);
	}

	@Override
	public Ride acceptRide(Long id) {
		return this.rideRepository.acceptRide(id);
	}

	public Page<Ride> findAll(Long driverId, Pageable page, LocalDateTime from, LocalDateTime to){
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
}
