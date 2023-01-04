package com.reesen.Reesen.service.interfaces;
import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.CreateRideDTO;
import com.reesen.Reesen.dto.RideDTO;
import com.reesen.Reesen.dto.UserRidesDTO;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.Optional;
import com.reesen.Reesen.model.WorkingHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

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

	Page<Ride> findAll(Long driverId, Pageable page, Date from, Date to);
	Page<Ride> findAllRidesForPassenger(Long passengerId, Pageable page, Date from, Date to);

	Page<Ride> findAllForUserWithRole(Long userId, Pageable page, Date from, Date to, Role role);

	Ride findPassengerActiveRide(Long passengerId);

	Set<Route> getLocationsByRide (Long ride_id);

    Set<UserRidesDTO> getFilteredRides(Page<Ride> userRides, Long driverId);

	UserRidesDTO getFilteredRide(Ride ride, Long driverId);
}
