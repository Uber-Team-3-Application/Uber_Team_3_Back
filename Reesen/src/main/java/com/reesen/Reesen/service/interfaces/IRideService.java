package com.reesen.Reesen.service.interfaces;
import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Deduction;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.*;

import java.util.Date;

public interface IRideService {

	// TODO: JELENA
	Ride findOne(Long id);

	RideDTO createRideDTO(CreateRideDTO rideDTO, Long passengerId);

	Ride findDriverActiveRide(Long driverId);

	RideDTO withdrawRide(Long id);
	// TODO: ------> JELENA

	// TODO: VELJA
	RideDTO panicRide(Long rideId, String reason, Long id); // OK

	RideDTO cancelRide(Long id, String reason); // OK

	RideDTO endRide(Long id); // OK

	RideDTO acceptRide(Long id); // OK

	List<Ride> findAll(); // OK

	Page<Ride> findAll(Long driverId, Pageable page, Date from, Date to); // OK
	// TODO: ---> VELJA



	//TODO: VUGA
	Page<Ride> findAllRidesForPassenger(Long passengerId, Pageable page, Date from, Date to);// TODO:FINISHED

	Page<Ride> findAllForUserWithRole(Long userId, Pageable page, Date from, Date to, Role role);// TODO:FINISHED

	Set<PassengerRideDTO> getFilteredRides(Page<Ride> userRides, Long driverId); //TODO:FINISHED

	PassengerRideDTO getFilteredRide(Ride ride, Long driverId); //TODO:FINISHED

	ReportSumAverageDTO getReport(ReportRequestDTO reportRequestDTO);

	Ride findPassengerActiveRide(Long passengerId); // TODO:FINISHED

	LinkedHashSet<Route> getLocationsByRide (Long rideId);// TODO:FINISHED
	//TODO: ----> VUGA


	//TODO: VEKSON
	ReportSumAverageDTO getReportForDriver(ReportRequestDTO reportRequestDTO); // FINISHED
	ReportSumAverageDTO filterReports(List<ReportDTO<Double>> reportDTOS, long totalDays); // FINISHED
	ReportSumAverageDTO filterTotalRidesReports(List<ReportDTO<Long>> reportDTOS, long totalDays); // FINISHED
	//TODO: ---> VEKSON

	// TODO: VUGA
	double calculateDistance(Location departure, Location destination);
	//TODO: ---> VUGA

	Ride save(Ride ride);
	// TODO: JELENA
	boolean validateCreateRideDTO(CreateRideDTO createRideDTO);

	boolean checkForPendingRide(Long passengerId);

	RideDTO startRide(Long id);

    ReportSumAverageDTO getReportForPassenger(ReportRequestDTO reportRequestDTO);

    List<RideWithVehicleDTO> getALlActiveRides();
    // TODO: -----> JELENA


}