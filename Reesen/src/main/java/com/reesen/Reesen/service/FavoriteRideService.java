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
		Set<FavoriteRide> allRides = passenger.getFavouriteRoutes();
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
		Set<RouteDTO> locationsDTOs = favouriteRide.getLocations();
		LinkedHashSet<Route> locations = new LinkedHashSet<>();
		for(RouteDTO routeDTO: locationsDTOs){
			Route route = new Route();
			Location departure = new Location(routeDTO.getDeparture().getLatitude(), routeDTO.getDeparture().getLongitude(), routeDTO.getDeparture().getAddress());
			this.locationService.save(departure);
			route.setDeparture(departure);
			Location destination = new Location(routeDTO.getDestination().getLatitude(), routeDTO.getDestination().getLongitude(), routeDTO.getDestination().getAddress());
			this.locationService.save(destination);
			route.setDestination(destination);
			locations.add(route);
			this.routeRepository.save(route);
		}
		Passenger passenger = this.passengerService.findOne(passengerId).get();
		Set<FavoriteRide> allRides = passenger.getFavouriteRoutes();
		for(FavoriteRide fav: allRides)
		{
			ride.setPassengers(this.favoriteRouteRepository.findPassengerByRideId(fav.getId()));
			LinkedHashSet<Route> routes = this.favoriteRouteRepository.getLocationsByRide(fav.getId());
			for(Route route: routes)
			{
				Location departure = this.routeRepository.getDepartureByRoute(route).get();
				Location destination = this.routeRepository.getDestinationByRoute(route).get();
				route.setDeparture(departure);
				route.setDestination(destination);
			}
			ride.setLocations(routes);
		}
		allRides.add(ride);
		passenger.setFavouriteRoutes(allRides);
		passengerService.save(passenger);
		ride.setLocations(locations);
		ride.setVehicleType(this.vehicleTypeRepository.findByName(VehicleName.valueOf(favouriteRide.getVehicleType())));
		ride.setBabyAccessible(favouriteRide.isBabyTransport());
		ride.setPetAccessible(favouriteRide.isPetTransport());
		Set<UserDTO> passengersDTOs = favouriteRide.getPassengers();
		Set<Passenger> passengers = new HashSet<>();
		for(UserDTO userDTO: passengersDTOs){
			passengers.add(this.passengerRepository.findByEmail(userDTO.getEmail()));
		}
		passengers.add(passenger);
		ride.setPassengers(passengers);
		ride.setFavoriteName(favouriteRide.getFavoriteName());
		return new FavoriteRideDTO(this.favoriteRouteRepository.save(ride));
	}

	@Override
	public Set<FavoriteRideDTO> getFavouriteRides(Long id) {
		Passenger passenger = this.passengerService.findOne(id).get();
		Set<FavoriteRide> allRides = passenger.getFavouriteRoutes();
		Set<FavoriteRideDTO> response = new HashSet<>();
		for(FavoriteRide ride: allRides)
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
			response.add(new FavoriteRideDTO(ride));
		}
		return response;
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

}
