package com.reesen.Reesen.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.repository.RideRepository;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.springframework.stereotype.Service;

@Service
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


}
