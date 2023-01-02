package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Route;

import java.util.Optional;

public interface IRouteService {
	Route findOne(Long id);
	Route save(Route route);
	Optional<Location> getDestinationByRoute(Route route);
	Optional<Location> getDepartureByRoute(Route route);

}
