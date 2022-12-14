package com.reesen.Reesen.service.interfaces;
import com.reesen.Reesen.dto.CreateRideDTO;
import com.reesen.Reesen.dto.RideDTO;
import com.reesen.Reesen.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.Optional;
import com.reesen.Reesen.model.WorkingHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;

public interface IRideService {
	Optional<Ride> findOne(Long id);
	Ride save(Ride ride);

	RideDTO createRideDTO(CreateRideDTO rideDTO);

	Optional<Ride> findDriverActiveRide(Long driverId);

	Ride withdrawRide(Ride ride);

	Ride panicRide(Ride ride, String reason);

	Ride cancelRide(Ride ride, String reason);

	Ride endRide(Ride ride);

	Ride acceptRide(Ride ride);

	Page<Ride> findAll(Long driverId, Pageable page, LocalDateTime from, LocalDateTime to);

	Ride findPassengerActiveRide(Long passengerId);
}
