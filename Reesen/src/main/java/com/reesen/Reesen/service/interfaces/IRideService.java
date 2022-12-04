package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Ride;

public interface IRideService {
	Ride findOne(Long id);
	Ride save(Ride ride);
}
