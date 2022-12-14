package com.reesen.Reesen.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.repository.RideRepository;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public class RideService implements IRideService {
	
    private final RideRepository rideRepository;

    @Autowired
    public RideService(RideRepository rideRepository){
        this.rideRepository = rideRepository;
    }

	@Override
	public Ride findOne(Long id) {
		return this.rideRepository.findById(id).orElseGet(null);
	}

	@Override
	public Ride save(Ride ride) {
		return this.rideRepository.save(ride);
	}

	@Override
	public Page<Ride> findAllPassengerRides(Long id, Pageable page, Date from, Date to) {
		if(from == null && to == null)
			return this.rideRepository.findAllPassengerRides(id, page);
		if(to != null && from == null)
			return this.rideRepository.findAllPassengerRidesBefore(id, to, page);
		if(to == null)
			return this.rideRepository.findAllPassengerRidesAfter(id, from, page);
		return this.rideRepository.findAllPassengerRidesInDateRange(id, from, to, page);

	}

}
