package com.reesen.Reesen.service;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.Enums.TypeOfReport;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.handlers.RideHandler;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.repository.*;
import com.reesen.Reesen.service.interfaces.ILocationService;
import com.reesen.Reesen.service.interfaces.IWorkingHoursService;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.springframework.beans.factory.annotation.Autowired;
import com.reesen.Reesen.service.interfaces.IRideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
	private final IWorkingHoursService workingHoursService;
	private final ILocationService locationService;
	private final DeductionRepository deductionRepository;
	private final ReviewRepository reviewRepository;
	private final ScheduledExecutorService executor;
	private final PassengerService passengerService;

	private SimpMessagingTemplate simpMessagingTemplate;
	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
	private ScheduledFuture scheduledFuture;

	@Autowired
	public RideService(RideRepository rideRepository, RouteRepository routeRepository, PassengerRepository passengerRepository, VehicleTypeRepository vehicleTypeRepository, PanicRepository panicRepository, UserRepository userRepository, DriverRepository driverRepository, IWorkingHoursService workingHoursService, ILocationService locationService, DeductionRepository deductionRepository, ReviewRepository reviewRepository, PassengerService passengerService, SimpMessagingTemplate simpMessagingTemplate) {
		this.rideRepository = rideRepository;
		this.routeRepository = routeRepository;
		this.passengerRepository = passengerRepository;
		this.vehicleTypeRepository = vehicleTypeRepository;
		this.panicRepository = panicRepository;
		this.userRepository = userRepository;
		this.driverRepository = driverRepository;
		this.workingHoursService = workingHoursService;
		this.locationService = locationService;
		this.deductionRepository = deductionRepository;
		this.reviewRepository = reviewRepository;
		this.passengerService = passengerService;
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.executor = Executors.newScheduledThreadPool(1);
	}


	@Override
	public Ride findOne(Long id) {
		Ride ride = this.rideRepository.findById(id).orElse(null);
		if (ride != null) {
			ride.setDriver(this.driverRepository.findDriverByRidesContaining(ride).orElse(null));
		}
		if (this.rideRepository.findById(id).isPresent()) {
			ride = this.rideRepository.findById(id).get();
			ride.setDriver(this.rideRepository.findDriverByRideId(id));
			ride.setPassengers(this.rideRepository.findPassengerByRideId(id));
			LinkedHashSet<Route> routes = this.rideRepository.getLocationsByRide(id);
			for (Route route : routes) {
				Location departure = this.routeRepository.getDepartureByRoute(route).get();
				Location destination = this.routeRepository.getDestinationByRoute(route).get();
				route.setDeparture(departure);
				route.setDestination(destination);
			}
			ride.setLocations(routes);
			if(ride.getScheduledTime() != null){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(ride.getScheduledTime());
				calendar.add(Calendar.MINUTE, -60);
				ride.setScheduledTime(calendar.getTime());
			}
			return ride;
		}
		return null;
	}
	@Override
	public Ride save(Ride ride) {
		return this.rideRepository.save(ride);
	}

	@Override
	public RideDTO createRideDTO(CreateRideDTO rideDTO, Long passengerId) {
		if (rideDTO.getScheduledTime() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR, 5);
			if (rideDTO.getScheduledTime().after(calendar.getTime()))
				return null;
		}
		Ride ride = new Ride();
		Set<RouteDTO> locationsDTOs = rideDTO.getLocations();
		LinkedHashSet<Route> locations = new LinkedHashSet<>();
		for (RouteDTO routeDTO : locationsDTOs) {
			Route route = new Route();
			Location departure = new Location(routeDTO.getDeparture().getLatitude(), routeDTO.getDeparture().getLongitude(), routeDTO.getDeparture().getAddress());
			departure = this.locationService.save(departure);

			route.setDeparture(departure);
			Location destination = new Location(routeDTO.getDestination().getLatitude(), routeDTO.getDestination().getLongitude(), routeDTO.getDestination().getAddress());
			destination = this.locationService.save(destination);
			route.setDestination(destination);

			route = this.routeRepository.save(route);
			locations.add(route);
		}

		ride.setLocations(locations);
		ride.setVehicleType(this.vehicleTypeRepository.findByName(VehicleName.getVehicleName(rideDTO.getVehicleType())));
		ride.setBabyAccessible(rideDTO.isBabyTransport());
		ride.setPetAccessible(rideDTO.isPetTransport());
		Set<UserDTO> passengersDTOs = rideDTO.getPassengers();
		Set<Passenger> passengers = new HashSet<>();
		for (UserDTO userDTO : passengersDTOs) {
			Passenger pass = this.passengerRepository.findByEmail(userDTO.getEmail());
			if (pass != null)
				passengers.add(pass);
		}
		if(this.passengerRepository.findById(passengerId).isPresent())
			passengers.add(this.passengerRepository.findById(passengerId).get());
		else
			return null;
		ride.setPassengers(passengers);
		ride.setStatus(RideStatus.PENDING);

		if (rideDTO.getScheduledTime() == null) {
			Object[] result = this.findSuitableDriver(ride);
			if (result[0] == null) {
				ride.setStatus(RideStatus.REJECTED);
				for(Passenger passenger: ride.getPassengers())
					simpMessagingTemplate.convertAndSend("/topic/passenger/ride/" + passenger.getId(), "No suitable driver found!");

			} else {
				ride.setDriver((Driver) result[0]);
				ride.setEstimatedTime((Double) result[1]);
				ride.setTotalPrice(this.calculateDistance(this.locationService.getFirstLocation(ride.getLocations()), this.locationService.getLastLocation(ride.getLocations())) * ride.getVehicleType().getPricePerKm());
			}
		} else {
			ride.setStatus(RideStatus.SCHEDULED);
		}

		ride.setScheduledTime(rideDTO.getScheduledTime());
		Ride newRide = this.rideRepository.save(ride);
		ride.setId(newRide.getId());
		for (Passenger passenger : ride.getPassengers()) {
			// must do this because of LAZY
			Set<Ride> passengersRides = this.passengerRepository.getPassengerRides(passenger.getId());
			passengersRides.add(newRide);
			passenger.setRides(passengersRides);
			this.passengerRepository.save(passenger);
		}
		if (ride.getDriver() != null) {
			Driver driver = ride.getDriver();
			Set<Ride> driversRides = this.driverRepository.getDriverRides(driver.getId());
			driversRides.add(newRide);
			driver.setRides(driversRides);
			this.driverRepository.save(driver);
			WebSocketSession session = RideHandler.driverSessions.get(ride.getDriver().getId().toString());
			if(session != null) {

				RideHandler.notifyChosenDriver(session, new RideDTO(ride));
			}
			else {
				System.out.println(ride.getDriver().getId());

				simpMessagingTemplate.convertAndSend("/topic/driver/ride/" + ride.getDriver().getId(), new RideDTO(ride));
			}
		} else if (rideDTO.getScheduledTime() == null) {
			for(Passenger passenger: ride.getPassengers())
				simpMessagingTemplate.convertAndSend("/topic/passenger/ride/" + passenger.getId(), "No suitable driver found!");
			return null;
		}

		return new RideDTO(ride);
	}

	@Scheduled(fixedDelay = 5000)  // schedule to run every minute at 15th second
	private void scheduleRide() {
		Set<Ride> rides = rideRepository.findAllByScheduledTimeIsNotNullAndStatus(RideStatus.SCHEDULED);
		Date now = new Date();
		for(Ride ride : rides){

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(ride.getScheduledTime());
			calendar.add(Calendar.MINUTE, -75);
			Date scheduledTimeMinus15 = calendar.getTime();
			if (scheduledTimeMinus15.before(now)) {
				Ride rideFull = findOne(ride.getId());
				Object[] result = this.findSuitableDriver(rideFull);
				if (result[0] == null) {
					rideFull.setStatus(RideStatus.REJECTED);
				} else {
					rideFull.setDriver((Driver) result[0]);
					rideFull.setEstimatedTime((Double) result[1]);
					rideFull.setTotalPrice(this.calculateDistance(this.locationService.getFirstLocation(rideFull.getLocations()), this.locationService.getLastLocation(rideFull.getLocations())) * rideFull.getVehicleType().getPricePerKm());
				}
				if (rideFull.getDriver() != null) {
					Driver driver = rideFull.getDriver();
					Set<Ride> driversRides = this.driverRepository.getDriverRides(driver.getId());
					driversRides.add(rideFull);
					driver.setRides(driversRides);
					this.driverRepository.save(driver);
					WebSocketSession session = RideHandler.driverSessions.get(rideFull.getDriver().getId().toString());
					if(session != null) {

						RideHandler.notifyChosenDriver(session, new RideDTO(rideFull));
					}
					else {
						simpMessagingTemplate.convertAndSend("/topic/driver/ride/" + rideFull.getDriver().getId(), new RideDTO(rideFull));
					}
				} else  {
					for(Passenger passenger: rideFull.getPassengers())
						simpMessagingTemplate.convertAndSend("/topic/passenger/ride/" + passenger.getId(), "No suitable driver found!");
				}
			}
		}
	}

	private Object[] findSuitableDriver(Ride ride) {

		Object[] result = new Object[2];
		result[0] = null;
		result[1] = null;

		Driver bestDriver = new Driver();
		int minimumMinutes = Integer.MAX_VALUE;

		List<Driver> availableDrivers = this.driverRepository.findAllByIsActive(true);
		if (availableDrivers.isEmpty()) return result;

		List<Driver> suitableDrivers = new ArrayList<>();
		for (Driver driver : availableDrivers) {
			if (this.workingHoursService.getTotalHoursWorkedInLastDay(driver.getId()).toHours() >= 8) continue;
			//if(this.getRejectedRidesForDriver(driver.getId(), ride.getPassengers().iterator().next().getId())) continue;
			if (this.findDriverScheduledRide(driver.getId()).isPresent()) continue;
			Vehicle vehicle = driverRepository.getVehicle(driver.getId());
			//if(ride.getVehicleType().equals(vehicle.getType())) continue;
			if (ride.isBabyAccessible())
				if (!vehicle.isBabyAccessible()) continue;
			if (ride.isPetAccessible())
				if (!vehicle.isPetAccessible()) continue;
			if (ride.getPassengers().size() > vehicle.getPassengerSeats()) continue;
			suitableDrivers.add(driver);
		}

		if (suitableDrivers.isEmpty()) return result;

		for (Driver driver : suitableDrivers) {
			Vehicle vehicle = this.driverRepository.getVehicle(driver.getId());
			int minutes = 0;
			Ride currentRide = this.findDriverActiveRide(driver.getId());
			if (currentRide != null) {
				minutes += (this.calculateDistance(vehicle.getCurrentLocation(), locationService.getLastLocation(currentRide.getLocations())) / 80) * 60 * 2;
				minutes += (this.calculateDistance(locationService.getLastLocation(currentRide.getLocations()), locationService.getFirstLocation(ride.getLocations())) / 80) * 60 * 2;
			} else {
				minutes += (this.calculateDistance(vehicle.getCurrentLocation(), locationService.getFirstLocation(ride.getLocations())) / 80) * 60 * 2;
			}
			if ((int) minutes < minimumMinutes) {
				bestDriver = driver;
				minimumMinutes = (int) minutes;
			}
		}

		result[0] = bestDriver;
		result[1] = (minimumMinutes + (this.calculateDistance(locationService.getFirstLocation(ride.getLocations()), locationService.getLastLocation(ride.getLocations())) / 80) * 60 * 2);

		return result;
	}

	private Optional<Ride> findDriverScheduledRide(Long driverId) {
		return this.rideRepository.findRideByDriverIdAndStatus(driverId, RideStatus.ACCEPTED);
	}

	@Override
	public Ride findDriverActiveRide(Long driverId) {
		Ride ride = null;
		if (this.rideRepository.findRideByDriverIdAndStatus(driverId, RideStatus.STARTED).isPresent()) {
			ride = this.rideRepository.findRideByDriverIdAndStatus(driverId, RideStatus.STARTED).get();
			return this.findOne(ride.getId());
		}
		return ride;
	}

	@Override
	public RideDTO withdrawRide(Long id) {
		Optional<Ride> optionalRide = rideRepository.findById(id);
		if (optionalRide.isEmpty()) {
			return null;
		}
		Ride ride = this.findOne(id);
		ride.setStatus(RideStatus.CANCELED);
		rideRepository.save(ride);
		return new RideDTO(ride);
	}

	@Override
	public RideDTO panicRide(Long id, String reason, Long passengerId) {
		Optional<Ride> optionalRide = rideRepository.findById(id);
		if (optionalRide.isEmpty()) {
			return null;
		}
		Ride ride = this.findOne(id);
		ride.setStatus(RideStatus.FINISHED);
		ride.setPanicPressed(true);
		ride.setTimeOfEnd(new Date());

		LinkedHashSet<Route> locations = this.rideRepository.getLocationsByRide(ride.getId());
		for (Route route : locations) {
			route.setDeparture(this.routeRepository.getDepartureByRoute(route).get());
			route.setDestination(this.routeRepository.getDestinationByRoute(route).get());
		}
		ride.setLocations(locations);
		Ride newRide = this.rideRepository.save(ride);
		Optional<Driver> driver = this.driverRepository.findDriverByRidesContaining(newRide);
		if (driver.isPresent())
			newRide.setDriver(driver.get());
		else
			newRide.setDriver(null);
		Set<Passenger> ridePassengers = this.passengerRepository.findPassengersByRidesContaining(ride);
		newRide.setPassengers(ridePassengers);
		Long vehicleTypeId = this.rideRepository.getVehicleTypeId(ride.getId());
		VehicleType type = this.vehicleTypeRepository.findById(vehicleTypeId).get();
		newRide.setVehicleType(type);
		LinkedHashSet<Route> newLocations = this.rideRepository.getLocationsByRide(ride.getId());
		for (Route route : newLocations) {
			route.setDeparture(this.routeRepository.getDepartureByRoute(route).get());
			route.setDestination(this.routeRepository.getDestinationByRoute(route).get());
		}
		newRide.setLocations(newLocations);
		User user = userRepository.findById(passengerId).orElse(null);
		if (user == null)
			return null;
		this.panicRepository.save(new Panic(new Date(), reason, newRide, user));

		Long adminId = this.userRepository.findAdmin(Role.ADMIN);
		WebSocketSession webSocketSession = RideHandler.adminSessions.get(adminId.toString());
		if(webSocketSession != null) {
			RideHandler.notifyAdminAboutPanic(webSocketSession, new RideDTO(ride));
		}else {
			simpMessagingTemplate.convertAndSend("/topic/admin/panic/" + adminId, new RideDTO(ride));
		}
		if(user.getRole() == Role.DRIVER) {
			WebSocketSession driverSession = RideHandler.driverSessions.get(adminId.toString());
			if (driverSession != null) {
				RideHandler.notifyAdminAboutPanic(driverSession, new RideDTO(ride));
			} else {
				simpMessagingTemplate.convertAndSend("/topic/panic/" + user.getId(), new RideDTO(ride));
			}
		}else{
			WebSocketSession passengerSession = RideHandler.passengerSessions.get(adminId.toString());
			if (passengerSession != null) {
				RideHandler.notifyAdminAboutPanic(passengerSession, new RideDTO(ride));
			} else {
				simpMessagingTemplate.convertAndSend("/topic/panic/" + user.getId(), new RideDTO(ride));
			}
		}


		return new RideDTO(newRide);
	}

	@Override
	public RideDTO cancelRide(Long id, String reason) {
		Optional<Ride> optionalRide = rideRepository.findById(id);
		if (optionalRide.isEmpty()) {
			return null;
		}
		Ride ride = this.findOne(id);
		ride.setStatus(RideStatus.CANCELED);
		rideRepository.updateRideStatus(ride.getId(), RideStatus.CANCELED);
		Deduction deduction = deductionRepository.save(new Deduction(ride, ride.getDriver(), reason, LocalDateTime.now()));
		ride.setDeduction(deduction);
		rideRepository.save(ride);
		List<WebSocketSession> sessions = new ArrayList<>();
		for(Passenger passenger: ride.getPassengers()){
			WebSocketSession webSocketSession = RideHandler.passengerSessions.get(passenger.getId().toString());
			if(webSocketSession != null){
				sessions.add(webSocketSession);
			}
		}
		if(!sessions.isEmpty()) {
			RideHandler.notifyPassengerAboutDeclinedRide(sessions, new RideDTO(ride));
		}
		for(Passenger p: ride.getPassengers()){
			simpMessagingTemplate.convertAndSend("/topic/passenger/ride/"+p.getId(), new RideDTO(ride));
		}
		return new RideDTO(ride);
	}

	@Override
	public RideDTO endRide(Long id) {
		Optional<Ride> optionalRide = rideRepository.findById(id);
		if (optionalRide.isEmpty()) {
			return null;
		}
		Ride ride = this.findOne(id);
		ride.setStatus(RideStatus.FINISHED);
		ride.setTimeOfEnd(new Date());
		rideRepository.save(ride);

		List<WebSocketSession> sessions = new ArrayList<>();
		for(Passenger passenger: ride.getPassengers()){
			WebSocketSession webSocketSession = RideHandler.passengerSessions.get(passenger.getId().toString());
			if(webSocketSession != null){
				sessions.add(webSocketSession);
			}
		}
		if(!sessions.isEmpty()) {
			RideHandler.notifyPassengersAboutEndRide(sessions, new RideDTO(ride));
		}
		WebSocketSession driverSession = RideHandler.driverSessions.get(ride.getDriver().getId().toString());
		if(driverSession != null) {
			RideHandler.notifyDriverAboutEndRide(driverSession, new RideDTO(ride));
		}
		sessions.add(RideHandler.driverSessions.get(ride.getDriver().getId().toString()));

		for(Passenger p: ride.getPassengers()){
			simpMessagingTemplate.convertAndSend("/topic/passenger/end-ride/"+p.getId(), new RideDTO(ride));
		}
		simpMessagingTemplate.convertAndSend("/topic/driver/end-ride/"+ride.getDriver().getId(), new RideDTO(ride));
		Long adminId = this.userRepository.findAdmin(Role.ADMIN);
		WebSocketSession adminSession = RideHandler.driverSessions.get(adminId.toString());
		if(adminSession != null){
			RideHandler.notifyAdminAboutEndRide(adminSession, new RideDTO(ride));
		}
		simpMessagingTemplate.convertAndSend("/topic/admin/end-ride/"+adminId, new RideDTO(ride));


		return new RideDTO(ride);
	}

	@Override
	public RideDTO acceptRide(Long id) {
		Optional<Ride> optionalRide = rideRepository.findById(id);
		if (optionalRide.isEmpty()) {
			return null;
		}
		Ride ride = this.findOne(id);
		ride.setStatus(RideStatus.ACCEPTED);
		rideRepository.save(ride);
		if(ride.getScheduledTime() == null) {
			List<WebSocketSession> sessions = new ArrayList<>();
			for (Passenger passenger : ride.getPassengers()) {
				WebSocketSession webSocketSession = RideHandler.passengerSessions.get(passenger.getId().toString());
				if (webSocketSession != null) {
					sessions.add(webSocketSession);
				}
			}
			if (!sessions.isEmpty()) {
				RideHandler.notifyPassengerAboutAcceptedRide(sessions, new RideDTO(ride));
			}
			for (Passenger p : ride.getPassengers()) {
				simpMessagingTemplate.convertAndSend("/topic/passenger/ride/" + p.getId(), new RideDTO(ride));
			}
			WebSocketSession session = RideHandler.driverSessions.get(ride.getDriver().getId().toString());
			if(session != null){
				RideHandler.notifyDriverAboutAcceptedRide(session, new RideDTO(ride));

			}
			simpMessagingTemplate.convertAndSend("/topic/driver/ride/" + ride.getDriver().getId(), new RideDTO(ride));


		} else {
			scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
				List<WebSocketSession> sessions = new ArrayList<>();
				for (Passenger passenger : ride.getPassengers()) {
					WebSocketSession webSocketSession = RideHandler.passengerSessions.get(passenger.getId().toString());
					if (webSocketSession != null) {
						sessions.add(webSocketSession);
					}
				}
				if (!sessions.isEmpty()) {
					RideHandler.notifyPassengerAboutAcceptedRide(sessions, new RideDTO(ride));
				}
				for (Passenger p : ride.getPassengers()) {
					simpMessagingTemplate.convertAndSend("/topic/passenger/ride/" + p.getId(), "You have a scheduled ride!");
				}
				if(ride.getScheduledTime().before(new Date()))
					scheduledFuture.cancel(false);
			}, 0, 5, TimeUnit.MINUTES);
		}


		return new RideDTO(ride);


	}

	@Override
	public List<Ride> findAll() {
		return this.rideRepository.findAll();
	}

	public Page<Ride> findAll(Long driverId, Pageable page, Date from, Date to) {
		if (from == null && to == null)
			return this.rideRepository.findAllByDriverId(driverId, page);
		if (to != null && from == null)
			return this.rideRepository.findAllByDriverIdAndTimeOfEndBefore(driverId, to, page);
		if (to == null)
			return this.rideRepository.findAllByDriverIdAndTimeOfStartAfter(driverId, from, page);

		return this.rideRepository.findAllByDriverIdAndTimeOfStartAfterAndTimeOfEndBefore(driverId,
				from,
				to,
				page);

	}

	@Override
	public Page<Ride>
	findAllRidesForPassenger(Long passengerId, Pageable page, Date from, Date to) {
		Optional<Passenger> passenger = this.passengerRepository.findById(passengerId);
		if (passenger.isEmpty()) return null;

		if (from == null && to == null)
			return this.rideRepository.findAllRidesByPassengerId(passengerId, page);
		if (to != null && from == null)
			return this.rideRepository.findAllRidesByPassengerIdAndTimeOfEndBefore(passengerId, to, page);
		if (to == null)
			return this.rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfter(passengerId, from, page);

		return this.rideRepository.findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(passengerId,
				from,
				to,
				page);
	}

	@Override
	public Page<Ride> findAllForUserWithRole(Long userId, Pageable page, Date from, Date to, Role role) {
		if (role == Role.DRIVER) return this.findAll(userId, page, from, to);

		return this.findAllRidesForPassenger(userId, page, from, to);
	}

	@Override
	public Ride findPassengerActiveRide(Long passengerId) {
		Ride ride = this.rideRepository.findPassengerActiveRide(passengerId, RideStatus.STARTED);
		if (ride != null) {
			ride = this.findOne(ride.getId());
		}
		return ride;
	}

	@Override
	public LinkedHashSet<Route> getLocationsByRide(Long rideId) {
		return this.rideRepository.getLocationsByRide(rideId);
	}

	@Override
	public PassengerRideDTO getFilteredRide(Ride ride, Long driverId) {

		Set<Passenger> passengers_ = passengerRepository.findPassengersByRidesContaining(ride);
		ride.setPassengers(passengers_);

		Set<Review> reviews = this.reviewRepository.findAllByRideId(ride.getId());
		for (Review review : reviews) {
			review.setPassenger(this.passengerRepository.findbyReviewId(review.getId()));
		}

		ride.setReview(reviews);
		ride.setDeduction(deductionRepository.findDeductionByRide(ride).orElse(null));
		LinkedHashSet<Route> locations;
		locations = this.getLocationsByRide(ride.getId());
		for (Route location : locations) {
			location.setDestination(this.routeRepository.getDestinationByRoute(location).get());
			location.setDeparture(this.routeRepository.getDepartureByRoute(location).get());
		}

		ride.setLocations(locations);
		PassengerRideDTO rideDTO = new PassengerRideDTO();

		rideDTO = rideDTO.newInstance(ride);
		if (driverId != 0L)
			rideDTO.setDriver(new UserDTO(this.userRepository.findById(driverId).get()));
		else
			rideDTO.setDriver(new UserDTO(this.driverRepository.findDriverByRidesContaining(ride).get()));

		return rideDTO;
	}

	@Override
	public ReportSumAverageDTO getReport(ReportRequestDTO reportRequestDTO) {
		long diffInMillies = Math.abs(reportRequestDTO.getTo().getTime() - reportRequestDTO.getFrom().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (reportRequestDTO.getTypeOfReport() == TypeOfReport.RIDES_PER_DAY) {
			List<ReportDTO<Long>> reportDTOS = this.rideRepository.getRidesPerDayReport(reportRequestDTO.getFrom(), reportRequestDTO.getTo());

			return this.filterTotalRidesReports(reportDTOS, diff);
		} else if (reportRequestDTO.getTypeOfReport() == TypeOfReport.KILOMETERS_PER_DAY) {
			List<RideLocationWithTimeDTO> rideLocationWithTimeDTO =
					this.rideRepository.getAllRidesWithStartTimeBetween(reportRequestDTO.getFrom(),
							reportRequestDTO.getTo());
			List<ReportDTO<Double>> reportDTOS = new ArrayList<>();
			FilterRideLocations(rideLocationWithTimeDTO, reportDTOS);
			return this.filterReports(reportDTOS, diff);

		} else if (reportRequestDTO.getTypeOfReport() == TypeOfReport.MONEY_SPENT_PER_DAY) {
			List<ReportDTO<Double>> reportDTOS = this.rideRepository.getTotalCostPerDay(reportRequestDTO.getFrom(), reportRequestDTO.getTo());
			return this.filterReports(reportDTOS, diff);

		} else if (reportRequestDTO.getTypeOfReport() == TypeOfReport.MONEY_EARNED_PER_DAY) {
			List<ReportDTO<Double>> reportDTOS = this.rideRepository.getTotalCostPerDay(reportRequestDTO.getFrom(), reportRequestDTO.getTo());
			return this.filterReports(reportDTOS, diff);

		}

		return null;

	}

	@Override
	public ReportSumAverageDTO getReportForDriver(ReportRequestDTO reportRequestDTO) {
		long diffInMillies = Math.abs(reportRequestDTO.getTo().getTime() - reportRequestDTO.getFrom().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		long driverId = reportRequestDTO.getDriverId();

		if (reportRequestDTO.getTypeOfReport() == TypeOfReport.RIDES_PER_DAY) {
			List<ReportDTO<Long>> reportDTOS = this.rideRepository.getRidesPerDayForSpecificDriver(reportRequestDTO.getFrom(),
					reportRequestDTO.getTo(), driverId);

			return this.filterTotalRidesReports(reportDTOS, diff);
		} else if (reportRequestDTO.getTypeOfReport() == TypeOfReport.KILOMETERS_PER_DAY) {

			List<RideLocationWithTimeDTO> rideLocationWithTimeDTO =
					this.rideRepository.getRidesWithStartTimeBetweenForSpecificDriver(reportRequestDTO.getFrom(),
							reportRequestDTO.getTo(), driverId);

			List<ReportDTO<Double>> reportDTOS = new ArrayList<>();
			FilterRideLocations(rideLocationWithTimeDTO, reportDTOS);
			return this.filterReports(reportDTOS, diff);

		} else if (reportRequestDTO.getTypeOfReport() == TypeOfReport.MONEY_SPENT_PER_DAY) {

			List<ReportDTO<Double>> reportDTOS = this.rideRepository.getTotalCostPerDayForSpecificDriver(
					reportRequestDTO.getFrom(), reportRequestDTO.getTo(), driverId);
			return this.filterReports(reportDTOS, diff);

		} else if (reportRequestDTO.getTypeOfReport() == TypeOfReport.MONEY_EARNED_PER_DAY) {
			List<ReportDTO<Double>> reportDTOS = this.rideRepository.getTotalCostPerDayForDriver(
					reportRequestDTO.getFrom(), reportRequestDTO.getTo(), driverId);
			return this.filterReports(reportDTOS, diff);

		}

		return null;
	}

	private void FilterRideLocations(List<RideLocationWithTimeDTO> rideLocationWithTimeDTO, List<ReportDTO<Double>> reportDTOS) {
		for (RideLocationWithTimeDTO r : rideLocationWithTimeDTO) {
			Set<Route> routes = this.rideRepository.getLocationsByRide(r.getRideId());
			for (Route route : routes) {
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
	public ReportSumAverageDTO filterTotalRidesReports(List<ReportDTO<Long>> reportDTOS, long totalDays) {

		if (totalDays <= 0){
			throw new IllegalArgumentException("Total days must be positive");

		}

		ReportSumAverageDTO reportSumAverageDTO = new ReportSumAverageDTO();

		Map<Date, Double> reports = new LinkedHashMap<>();
		double sum = 0;
		for (ReportDTO<Long> report : reportDTOS) {
			Date date = getFormattedDate(report);
			if (reports.containsKey(date)) {
				reports.computeIfPresent(date, (k, v) -> v + (double) (report.getTotal()));
			} else {
				reports.put(date, (double) (report.getTotal()));
			}
			sum += report.getTotal();

		}
		reportSumAverageDTO.setResult(reports);
		reportSumAverageDTO.setSum(sum);
		reportSumAverageDTO.setAverage(sum / totalDays);
		return reportSumAverageDTO;
	}

	@Override
	public double calculateDistance(Location departure, Location destination) {
		if(departure == null || destination == null) return 0;
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

		if (totalDays <= 0) {
			throw new IllegalArgumentException("Total days must be positive");
		}

		ReportSumAverageDTO reportSumAverageDTO = new ReportSumAverageDTO();
		Map<Date, Double> reports = new LinkedHashMap<>();
		double sum = 0;
		for (ReportDTO<Double> report : reportDTOS) {
			Date date = getFormattedDate(report);
			if (reports.containsKey(date)) {
				reports.computeIfPresent(date, (k, v) -> v + report.getTotal());
			} else {
				reports.put(date, report.getTotal());
			}
			sum += report.getTotal();
		}
		reportSumAverageDTO.setResult(reports);
		reportSumAverageDTO.setSum(sum);
		reportSumAverageDTO.setAverage(sum / totalDays);

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
	public Set<PassengerRideDTO> getFilteredRides(Page<Ride> userRides, Long driverId) {

		Set<PassengerRideDTO> rides = new LinkedHashSet<>();
		for (Ride ride : userRides) {
			rides.add(this.getFilteredRide(ride, driverId));
		}
		return rides;
	}



	@Override
	public boolean validateCreateRideDTO(CreateRideDTO createRideDTO) {
		if (createRideDTO.getPassengers() == null ||
				createRideDTO.getLocations() == null ||
				createRideDTO.getVehicleType() == null) {
			return true;
		}
		for (UserDTO passenger : createRideDTO.getPassengers()) {
			if (passenger.getEmail() == null) {
				return true;
			}
		}
		for (RouteDTO location : createRideDTO.getLocations()) {
			if (location.getDeparture() == null || location.getDestination() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkForPendingRide(Long passengerId) {
		if (!this.rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.PENDING).isEmpty())
			return true;
		if(!this.rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.ACCEPTED).isEmpty())
			return true;
		if(!this.rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.STARTED).isEmpty())
			return true;
		if(!this.rideRepository.findAllRidesByPassengerIdAndRideStatus(passengerId, RideStatus.ACTIVE).isEmpty())
			return true;

		return false;
	}

	@Override
	public RideDTO startRide(Long id) {
		Optional<Ride> optionalRide = rideRepository.findById(id);
		if (optionalRide.isEmpty()) {
			return null;
		}
		Ride ride = this.findOne(id);
		ride.setStatus(RideStatus.STARTED);
		ride.setTimeOfStart(new Date());
		rideRepository.save(ride);
		List<WebSocketSession> sessions = new ArrayList<>();
		for(Passenger passenger: ride.getPassengers()){
			WebSocketSession webSocketSession = RideHandler.passengerSessions.get(passenger.getId().toString());
			if(webSocketSession != null){
				sessions.add(webSocketSession);
			}
		}
		if(!sessions.isEmpty()) {
			RideHandler.notifyPassengersAboutStartRide(sessions, new RideDTO(ride));
		}
		sessions.add(RideHandler.driverSessions.get(ride.getDriver().getId().toString()));

		for(Passenger p: ride.getPassengers()){
			simpMessagingTemplate.convertAndSend("/topic/passenger/start-ride/"+p.getId(), new RideDTO(ride));
		}
		simpMessagingTemplate.convertAndSend("/topic/driver/start-ride/"+ride.getDriver().getId(), new RideDTO(ride));

		return new RideDTO(ride);

	}

	@Override
	public ReportSumAverageDTO getReportForPassenger(ReportRequestDTO reportRequestDTO) {
		long diffInMillies = Math.abs(reportRequestDTO.getTo().getTime() - reportRequestDTO.getFrom().getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		long passengerId = reportRequestDTO.getDriverId();

		if (reportRequestDTO.getTypeOfReport() == TypeOfReport.RIDES_PER_DAY) {
			List<ReportDTO<Long>> reportDTOS = this.rideRepository.getRidesPerDayForSpecificPassenger(reportRequestDTO.getFrom(),
					reportRequestDTO.getTo(), passengerId);

			return this.filterTotalRidesReports(reportDTOS, diff);
		} else if (reportRequestDTO.getTypeOfReport() == TypeOfReport.KILOMETERS_PER_DAY) {

			List<RideLocationWithTimeDTO> rideLocationWithTimeDTO =
					this.rideRepository.getRidesWithStartTimeBetweenForSpecificPassenger(reportRequestDTO.getFrom(),
							reportRequestDTO.getTo(), passengerId);

			List<ReportDTO<Double>> reportDTOS = new ArrayList<>();
			FilterRideLocations(rideLocationWithTimeDTO, reportDTOS);
			return this.filterReports(reportDTOS, diff);

		} else if (reportRequestDTO.getTypeOfReport() == TypeOfReport.MONEY_SPENT_PER_DAY) {

			List<ReportDTO<Double>> reportDTOS = this.rideRepository.getTotalCostPerDayForSpecificPassenger(
					reportRequestDTO.getFrom(), reportRequestDTO.getTo(), passengerId);
			return this.filterReports(reportDTOS, diff);

		}

		return null;
	}

	@Override
	public List<RideWithVehicleDTO> getALlActiveRides() {
		return this.rideRepository.getAllActiveRides(RideStatus.ACTIVE, RideStatus.STARTED);
	}


}