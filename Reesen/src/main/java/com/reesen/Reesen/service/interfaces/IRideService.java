package com.reesen.Reesen.service.interfaces;
import com.reesen.Reesen.dto.CreateRideDTO;
import com.reesen.Reesen.dto.RideDTO;
import com.reesen.Reesen.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.Optional;

public interface IRideService {
	Optional<Ride> findOne(Long id);
	Ride save(Ride ride);

	RideDTO createRideDTO(CreateRideDTO rideDTO);

	Optional<Ride> findDriverActiveRide(Long driverId);

	Ride withdrawRide(Long id);

	Ride panicRide(Long id, String reason);

	Ride cancelRide(Long id, String reason);

	Ride endRide(Long id);

	Ride acceptRide(Long id);
}
