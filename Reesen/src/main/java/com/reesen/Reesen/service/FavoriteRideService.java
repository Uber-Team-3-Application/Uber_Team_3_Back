package com.reesen.Reesen.service;

import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.dto.FavoriteRideDTO;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.repository.*;
import com.reesen.Reesen.service.interfaces.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import com.reesen.Reesen.service.interfaces.IFavoriteRideService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class FavoriteRideService implements IFavoriteRideService {
	
	private final FavoriteRouteRepository favoriteRouteRepository;
	private final RouteRepository routeRepository;
	private final PassengerRepository passengerRepository;
	private final VehicleTypeRepository vehicleTypeRepository;
	private final ILocationService locationService;
	private PassengerService passengerService;
	
	@Autowired
	public FavoriteRideService(FavoriteRouteRepository favoriteRouteRepository, RouteRepository routeRepository, PassengerRepository passengerRepository, VehicleTypeRepository vehicleTypeRepository, ILocationService locationService, PassengerService passengerService) {
		this.favoriteRouteRepository = favoriteRouteRepository;
		this.routeRepository = routeRepository;
		this.passengerRepository = passengerRepository;
		this.vehicleTypeRepository = vehicleTypeRepository;
		this.locationService = locationService;
		this.passengerService = passengerService;
	}

	@Override
	public void remove(Long id) {
		this.favoriteRouteRepository.deleteById(id);
	}

	@Override
	public FavoriteRide findOne(Long id) {
		if(this.favoriteRouteRepository.findById(id).isPresent())
			return this.favoriteRouteRepository.findById(id).get();
		else
			return null;
	}

	@Override
	public FavoriteRide save(FavoriteRide favoriteRide) {
		return this.favoriteRouteRepository.save(favoriteRide);
	}


	@Override
	public void deleteFavouriteRides(Long id, Long passengerId) {
		Passenger passenger = this.passengerService.findOne(passengerId).get();
		Set<FavoriteRide> allRides = this.passengerService.getFavoriteRides(passenger.getId());
		FavoriteRide removeRide = null;
		for(FavoriteRide ride: allRides)
		{
			if(ride.getId() == id) removeRide = ride;
			ride.setPassengers(this.favoriteRouteRepository.findPassengerByRideId(ride.getId()));
			LinkedHashSet<Route> routes = this.favoriteRouteRepository.getLocationsByRide(ride.getId());
			for(Route route: routes)
			{
				Location departure = this.routeRepository.getDepartureByRoute(route).get();
				Location destination = this.routeRepository.getDestinationByRoute(route).get();
				route.setDeparture(departure);
				route.setDestination(destination);
			}
			ride.setLocations(routes);
		}
		allRides.remove(removeRide);
		passenger.setFavouriteRoutes(allRides);
		passengerService.save(passenger);
		this.favoriteRouteRepository.deleteById(id);
	}

	@Override
	public FavoriteRideDTO addFavouriteRide(CreateFavoriteRideDTO favouriteRide, Long passengerId) {
		FavoriteRide ride = new FavoriteRide();
		ride.setBabyAccessible(favouriteRide.isBabyTransport());
		ride.setPetAccessible(favouriteRide.isBabyTransport());
		ride.setVehicleType(this.vehicleTypeRepository.findByName(VehicleName.getVehicleName(favouriteRide.getVehicleType())));
		ride.setFavoriteName(favouriteRide.getFavoriteName());

		ride.setPassengers(new LinkedHashSet<>());
		for(UserDTO user: favouriteRide.getPassengers()){
			Optional<Passenger> passenger = passengerRepository.findById(user.getId());
			passenger.ifPresent(value -> ride.getPassengers().add(value));
		}
		ride.getPassengers().add(this.passengerService.findOne(passengerId).get());

		Route route = new Route();
		for(RouteDTO location: favouriteRide.getLocations()){
			Location departure = new Location(
					location.getDeparture().getLatitude(),
					location.getDeparture().getLongitude(),
					location.getDeparture().getAddress()
			);
			Location destination = new Location(
					location.getDestination().getLatitude(),
					location.getDestination().getLongitude(),
					location.getDestination().getAddress()
			);
			departure = this.locationService.save(departure);
			destination = this.locationService.save(destination);
			route.setDeparture(departure);
			route.setDestination(destination);
			routeRepository.save(route);
			Set<Route> routes = new HashSet<>();
			routes.add(route);
			ride.setLocations(routes);
			break;

		}

		FavoriteRide newFavoriteRide = favoriteRouteRepository.save(ride);

		Passenger passenger = this.passengerService.findOne(passengerId).get();
		Set<FavoriteRide> passengerFavoriteRides =this.passengerRepository.getFavoriteRides(passengerId);
		passenger.setFavouriteRoutes(passengerFavoriteRides);
		passenger.getFavouriteRoutes().add(newFavoriteRide);
		this.passengerRepository.save(passenger);


		return new FavoriteRideDTO(ride);
	}

	@Override
	public Set<FavoriteRideDTO> getFavouriteRides(Long id) {
		Set<FavoriteRideDTO> favoriteRides = new HashSet<>();
		Set<FavoriteRide> faves = favoriteRouteRepository.findAllByPassengerId(id);

		for(FavoriteRide ride: faves)
		{
			ride.setPassengers(this.favoriteRouteRepository.findPassengerByRideId(ride.getId()));
			LinkedHashSet<Route> routes = this.favoriteRouteRepository.getLocationsByRide(ride.getId());
			for(Route route: routes)
			{
				Location departure = this.routeRepository.getDepartureByRoute(route).get();
				Location destination = this.routeRepository.getDestinationByRoute(route).get();
				route.setDeparture(departure);
				route.setDestination(destination);
			}
			ride.setLocations(routes);
			favoriteRides.add(new FavoriteRideDTO(ride));
		}
		return favoriteRides;
	}

	@Override
	public boolean validateRideDTO(CreateFavoriteRideDTO createRideDTO) {
		if (createRideDTO.getFavoriteName() == null ||
				createRideDTO.getPassengers() == null ||
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
	public boolean checkForName(String favoriteName, Long id) {
		Set<FavoriteRideDTO> response = this.getFavouriteRides(id);
		for(FavoriteRideDTO ride: response)
		{
			if(ride.getFavoriteName() == favoriteName)
				return true;
		}
		return false;
	}

}
