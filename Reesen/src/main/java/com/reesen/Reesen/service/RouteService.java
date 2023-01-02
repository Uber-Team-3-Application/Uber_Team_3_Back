package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Location;
import org.springframework.beans.factory.annotation.Autowired;

import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.repository.RouteRepository;
import com.reesen.Reesen.service.interfaces.IRouteService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RouteService implements IRouteService{
	
    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository){
        this.routeRepository = routeRepository;
    }
	@Override
	public Route findOne(Long id) {
		return this.routeRepository.findById(id).orElseGet(null);
	}

	@Override
	public Route save(Route route) {
		return this.routeRepository.save(route);
	}

	@Override
	public Optional<Location> getDestinationByRoute(Route route) {
		return routeRepository.getDestinationByRoute(route);
	}

	@Override
	public Optional<Location> getDepartureByRoute(Route route) {
		return this.routeRepository.getDepartureByRoute(route);
	}

}
