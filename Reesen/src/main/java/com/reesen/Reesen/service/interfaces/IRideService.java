package com.reesen.Reesen.service.interfaces;
import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

import java.util.Date;

public interface IRideService {
	Ride findOne(Long id);
	Ride save(Ride ride);

	RideDTO createRideDTO(CreateRideDTO rideDTO);

	Ride findDriverActiveRide(Long driverId);

	Ride withdrawRide(Ride ride);

	Ride panicRide(Ride ride, String reason, Long id);

	Ride cancelRide(Ride ride, String reason);

	Ride endRide(Ride ride);

	Ride acceptRide(Ride ride);

	List<Ride> findAll();

	Page<Ride> findAll(Long driverId, Pageable page, Date from, Date to);
	Page<Ride> findAllRidesForPassenger(Long passengerId, Pageable page, Date from, Date to);

	Page<Ride> findAllForUserWithRole(Long userId, Pageable page, Date from, Date to, Role role);

	Ride findPassengerActiveRide(Long passengerId);

	LinkedHashSet<Route> getLocationsByRide (Long rideId);

    Set<UserRidesDTO> getFilteredRides(Page<Ride> userRides, Long driverId);

	UserRidesDTO getFilteredRide(Ride ride, Long driverId);

	ReportSumAverageDTO getReport(ReportRequestDTO reportRequestDTO);

	ReportSumAverageDTO getReportForDriver(ReportRequestDTO reportRequestDTO);

	ReportSumAverageDTO filterReports(List<ReportDTO<Double>> reportDTOS, long totalDays);
	ReportSumAverageDTO filterTotalRidesReports(List<ReportDTO<Long>> reportDTOS, long totalDays);

	Set<Review> findAllReviewsBySpecificDriverAndRide(Long rideId);


	double calculateDistance(Location departure, Location destination);

}
