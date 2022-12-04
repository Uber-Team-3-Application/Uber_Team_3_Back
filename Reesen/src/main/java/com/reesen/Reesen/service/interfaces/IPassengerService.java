package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Passenger;

public interface IPassengerService {
	Passenger save(Passenger passenger);
	Passenger findOne(Long id);
}
