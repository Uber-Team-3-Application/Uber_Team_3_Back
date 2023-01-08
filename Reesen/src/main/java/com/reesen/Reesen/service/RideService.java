package com.reesen.Reesen.service;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.Enums.TypeOfReport;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class RideService implements IRideService {
	
    private final RideRepository rideRepository;
	private final RouteRepository routeRepository;
	private final PassengerRepository passengerRepository;
	private final VehicleTypeRepository vehicleTypeRepository;
	private final PanicRepository panicRepository;
	private final UserRepository userRepository;
	private final DriverRepository driverRepository;

	private final DeductionRepository deductionRepository;
	private final ReviewRepository reviewRepository;


    @Autowired
    public RideService(RideRepository rideRepository, RouteRepository routeRepository, PassengerRepository passengerRepository, VehicleTypeRepository vehicleTypeRepository, PanicRepository panicRepository, UserRepository userRepository, DriverRepository driverRepository, DeductionRepository deductionRepository, ReviewRepository reviewRepository){
        this.rideRepository = rideRepository;
		this.routeRepository = routeRepository;
		this.passengerRepository = passengerRepository;
		this.vehicleTypeRepository = vehicleTypeRepository;
		this.panicRepository = panicRepository;
		this.userRepository = userRepository;
		this.driverRepository = driverRepository;
		this.deductionRepository = deductionRepository;
		this.reviewRepository = reviewRepository;
	}


	@Override
	public Optional<Ride> findOne(Long id) {
		return this.rideRepository.findById(id);
	}

	@Override
	public Ride save(Ride ride) {
		return this.rideRepository.save(ride);
	}

	@Override
	public RideDTO createRideDTO(CreateRideDTO rideDTO) {
		Ride ride = new Ride();
		ride.setId(Long.parseLong("546"));
		Set<RouteDTO> locationsDTOs = rideDTO.getLocations();
		Set<Route> locations = new HashSet<>();
		for(RouteDTO routeDTO: locationsDTOs){
			locations.add(this.routeRepository.findById(routeDTO.getId()).get());
		}
		ride.setLocations(locations);
		ride.setVehicleType(this.vehicleTypeRepository.findByName(VehicleName.valueOf(rideDTO.getVehicleType())));
		ride.setBabyAccessible(rideDTO.isBabyTransport());
		ride.setPetAccessible(rideDTO.isPetTransport());
		Set<UserDTO> passengersDTOs = rideDTO.getPassengers();
		Set<Passenger> passengers = new HashSet<>();
		for(UserDTO userDTO: passengersDTOs){
			passengers.add(this.passengerRepository.findByEmail(userDTO.getEmail()));
		}
		ride.setPassengers(passengers);
		return new RideDTO(this.rideRepository.save(ride));
	}

	@Override
	public Optional<Ride> findDriverActiveRide(Long driverId) {
		return this.rideRepository.findRideByDriverIdAndStatus(driverId, RideStatus.ACTIVE);
	}

	@Override
	public Ride withdrawRide(Ride ride) {
		ride.setStatus(RideStatus.WITHDRAWN);
		return ride;
	}

	@Override
	public Ride panicRide(Ride ride, String reason) {
		this.panicRepository.save(new Panic(new Date(), reason, ride, ride.getDriver()));
		ride.setStatus(RideStatus.FINISHED);
		return ride;
	}

	@Override
	public Ride cancelRide(Ride ride, String reason) {
		ride.setStatus(RideStatus.REJECTED);
		Deduction deduction = new Deduction(ride, ride.getDriver(), reason, LocalDateTime.now());
		ride.setDeduction(deduction);
		return ride;
	}

	@Override
	public Ride endRide(Ride ride) {
		ride.setStatus(RideStatus.FINISHED);
		return ride;
	}

	@Override
	public Ride acceptRide(Ride ride) {
		ride.setStatus(RideStatus.ACCEPTED);
		return ride;
	}

	public Page<Ride> findAll(Long driverId, Pageable page, Date from, Date to){
		if(from == null && to == null)
			return this.rideRepository.findAllByDriverId(driverId, page);
		if(to != null && from == null)
			return this.rideRepository.findAllByDriverIdAndTimeOfEndBefore(driverId, to, page);
		if(to == null)
			return this.rideRepository.findAllByDriverIdAndTimeOfStartAfter(driverId, from, page);

		return this.rideRepository.findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(driverId,
				from,
				to,
				page);

	}

	@Override
	public Page<Ride> findAllRidesForPassenger(Long passengerId, Pageable page, Date from, Date to) {
		Optional<Passenger> passenger = this.passengerRepository.findById(passengerId);
		if(passenger.isEmpty()) return null;

		if(from == null && to == null)
			return this.rideRepository.findAllRidesByPassengerId(passengerId, page);
		if(to != null && from == null)
			return this.rideRepository.findAllRidesByPassengerIdAndTimeOfEndBefore(passengerId, to, page);
		if(to == null)
			return this.rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfter(passengerId, from, page);

		return this.rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(passengerId,
				from,
				to,
				page);
	}

	@Override
	public Page<Ride> findAllForUserWithRole(Long userId, Pageable page, Date from, Date to, Role role) {
		if(role == Role.DRIVER) return this.findAll(userId, page, from, to);

		return this.findAllRidesForPassenger(userId, page, from ,to);
	}

	@Override
	public Ride findPassengerActiveRide(Long passengerId) {
		Passenger passenger = this.passengerRepository.findById(passengerId).get();
		for(Ride ride: passenger.getRides()){
			if(ride.getStatus() == RideStatus.ACTIVE)
				return ride;
		}
		return null;
	}

	@Override
	public Set<Route> getLocationsByRide(Long rideId) {
		return this.rideRepository.getLocationsByRide(rideId);
	}

	@Override
	public UserRidesDTO getFilteredRide(Ride ride, Long driverId){

		ride.setPassengers(passengerRepository.findPassengersByRidesContaining(ride));

		Set<Review> reviews = this.reviewRepository.findAllByRideId(ride.getId());
		for(Review review:reviews){
			review.setPassenger(this.passengerRepository.findbyReviewId(review.getId()));
		}

		ride.setReview(reviews);
		ride.setDeduction(deductionRepository.findDeductionByRide(ride).orElse(new Deduction()));
		Set<Route> locations;
		locations = this.getLocationsByRide(ride.getId());
		for (Route location : locations) {
			location.setDestination(this.routeRepository.getDestinationByRoute(location).get());
			location.setDeparture(this.routeRepository.getDepartureByRoute(location).get());
		}

		ride.setLocations(locations);
		UserRidesDTO rideDTO = new UserRidesDTO(ride);
		if(driverId != 0L)
			rideDTO.setDriver(new UserDTO(
					this.userRepository.findById(driverId).get()

			));

		else
			rideDTO.setDriver(new UserDTO(
					this.driverRepository.findDriverByRidesContaining(ride).get()
			));

		return rideDTO;
	}

	@Override
	public ReportSumAverageDTO getReport(ReportRequestDTO reportRequestDTO) {
		long diffInMillies = Math.abs(reportRequestDTO.getTo().getTime() - reportRequestDTO.getFrom().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if(reportRequestDTO.getTypeOfReport() == TypeOfReport.RIDES_PER_DAY){
			List<ReportDTO<Long>> reportDTOS = this.rideRepository.getRidesPerDayReport(reportRequestDTO.getFrom(), reportRequestDTO.getTo());

			return this.filterTotalRidesReports(reportDTOS, diff);
		}else if(reportRequestDTO.getTypeOfReport() == TypeOfReport.KILOMETERS_PER_DAY){
			List<RideLocationWithTimeDTO> rideLocationWithTimeDTO =
					this.rideRepository.getAllRidesWithStartTimeBetween(reportRequestDTO.getFrom(),
							reportRequestDTO.getTo());
			List<ReportDTO<Double>> reportDTOS = new ArrayList<>();
			FilterRideLocations(rideLocationWithTimeDTO, reportDTOS);
			return this.filterReports(reportDTOS, diff);

		}else if(reportRequestDTO.getTypeOfReport() == TypeOfReport.MONEY_SPENT_PER_DAY){
			List<ReportDTO<Double>> reportDTOS = this.rideRepository.getTotalCostPerDay(reportRequestDTO.getFrom(), reportRequestDTO.getTo());
			return this.filterReports(reportDTOS, diff);

		}else if(reportRequestDTO.getTypeOfReport() == TypeOfReport.MONEY_EARNED_PER_DAY){
			List<ReportDTO<Double>> reportDTOS = this.rideRepository.getTotalCostPerDay(reportRequestDTO.getFrom(), reportRequestDTO.getTo());
			return this.filterReports(reportDTOS, diff);

		}

		return null;

	}

	private void FilterRideLocations(List<RideLocationWithTimeDTO> rideLocationWithTimeDTO, List<ReportDTO<Double>> reportDTOS) {
		for(RideLocationWithTimeDTO r: rideLocationWithTimeDTO){
			Set<Route> routes = this.rideRepository.getLocationsByRide(r.getRideId());
			for(Route route: routes){
				route.setDeparture(this.routeRepository.getDepartureByRoute(route).get());
				route.setDestination(this.routeRepository.getDestinationByRoute(route).get());
			}
			r.setLocations(routes);
			List<Route> routesListed = r.getLocations().stream().toList();
			double distance = this.calculateDistance(routesListed.get(0).getDeparture(),
													routesListed.get(routesListed.size() - 1).getDestination());
			reportDTOS.add(new ReportDTO<>(r.getStartTime(), distance));
		}
	}

	@Override
	public ReportSumAverageDTO filterTotalRidesReports(List<ReportDTO<Long>> reportDTOS, long totalDays){
		ReportSumAverageDTO reportSumAverageDTO = new ReportSumAverageDTO();

		Map<Date, Double> reports = new LinkedHashMap<>();
		double sum = 0;
		for(ReportDTO<Long> report: reportDTOS){
			Date date = getFormattedDate(report);
			if(reports.containsKey(date)){
				reports.computeIfPresent(date, (k, v) -> v + (double)(report.getTotal()));
			}else{
				reports.put(date, (double)(report.getTotal()));
			}
			sum += report.getTotal();

		}
		reportSumAverageDTO.setResult(reports);
		reportSumAverageDTO.setSum(sum);
		reportSumAverageDTO.setAverage(sum/ totalDays);
		return reportSumAverageDTO;
	}

	@Override
	public double calculateDistance(Location departure, Location destination) {
		double theta = departure.getLongitude() - destination.getLongitude();
		double dist = Math.sin(Math.toRadians(departure.getLatitude())) * Math.sin(Math.toRadians(destination.getLatitude()))
				+ Math.cos(Math.toRadians(departure.getLatitude())) * Math.cos(Math.toRadians(destination.getLatitude())) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return dist;
	}

	@Override
	public ReportSumAverageDTO filterReports(List<ReportDTO<Double>> reportDTOS, long totalDays) {

		ReportSumAverageDTO reportSumAverageDTO = new ReportSumAverageDTO();
		Map<Date, Double> reports = new LinkedHashMap<>();
		double sum = 0;
		for(ReportDTO<Double> report: reportDTOS){
			Date date = getFormattedDate(report);
			if(reports.containsKey(date)){
				reports.computeIfPresent(date, (k, v) -> v + report.getTotal());
			}else{
				reports.put(date, report.getTotal());
			}
			sum += report.getTotal();
		}
		reportSumAverageDTO.setResult(reports);
		reportSumAverageDTO.setSum(sum);
		reportSumAverageDTO.setAverage(sum/ totalDays);

		return reportSumAverageDTO;
	}

	private Date getFormattedDate(ReportDTO report) {
		Date date = report.getDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String dateStr = sdf.format(date);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Set<UserRidesDTO> getFilteredRides(Page<Ride> userRides, Long driverId) {
		Set<UserRidesDTO> rides = new LinkedHashSet<>();
		for(Ride ride: userRides){

			rides.add(this.getFilteredRide(ride, driverId));
		}

		return rides;
	}

}
