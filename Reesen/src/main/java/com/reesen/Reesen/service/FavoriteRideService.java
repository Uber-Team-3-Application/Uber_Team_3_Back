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
	public void deleteFavouriteRides(Long id) {
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

		ride.setLocations(locations);
		ride.setVehicleType(this.vehicleTypeRepository.findByName(VehicleName.valueOf(favouriteRide.getVehicleType())));
		ride.setBabyAccessible(favouriteRide.isBabyTransport());
		ride.setPetAccessible(favouriteRide.isPetTransport());
		Set<UserDTO> passengersDTOs = favouriteRide.getPassengers();
		Set<Passenger> passengers = new HashSet<>();
		for(UserDTO userDTO: passengersDTOs){
			passengers.add(this.passengerRepository.findByEmail(userDTO.getEmail()));
		}
		Passenger favoritePassenger = this.passengerService.findOne(passengerId).get();
		Set<FavoriteRide> passengerFavoriteRides = this.passengerRepository.getFavoriteRides(passengerId);
		for(FavoriteRide favoriteRide :passengerFavoriteRides){
			favoriteRide = this.favoriteRouteRepository.findById(favoriteRide.getId()).get();
			
		}

		passengers.add(this.passengerService.findOne(passengerId).get());
		ride.setPassengers(passengers);
		ride.setFavoriteName(favouriteRide.getFavoriteName());
		FavoriteRide favRide = this.favoriteRouteRepository.save(ride);

		favoritePassenger.setFavouriteRoutes(passengerFavoriteRides);

		ride.setId(favRide.getId());
		return new FavoriteRideDTO(ride);
	}

	@Override
	public Set<FavoriteRideDTO> getFavouriteRides(Long id) {
		Set<FavoriteRideDTO> favoriteRides = new HashSet<>();
		Set<FavoriteRide> faves = favoriteRouteRepository.findAllByPassengerId(id);

		for(FavoriteRide ride: faves)
		{
			ride.setPassengers(this.favoriteRouteRepository.findPassengerByRideId(id));
			LinkedHashSet<Route> routes = this.favoriteRouteRepository.getLocationsByRide(id);
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

}
