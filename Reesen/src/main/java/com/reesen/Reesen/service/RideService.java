package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.CreateRideDTO;
import com.reesen.Reesen.dto.RideDTO;
import com.reesen.Reesen.dto.RouteDTO;
import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.repository.RideRepository;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RideService implements IRideService {
	
    private final RideRepository rideRepository;
	private final RouteRepository routeRepository;

    @Autowired
    public RideService(RideRepository rideRepository, RouteRepository routeRepository){
        this.rideRepository = rideRepository;
		this.routeRepository = routeRepository;
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

}
