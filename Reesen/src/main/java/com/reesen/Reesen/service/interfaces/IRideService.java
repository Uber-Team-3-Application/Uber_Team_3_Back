package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.WorkingHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;

public interface IRideService {
	Ride findOne(Long id);
	Ride save(Ride ride);

	Page<Ride> findAllPassengerRides(Long id, Pageable page, Date fromDate, Date toDate);
}