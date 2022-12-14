package com.reesen.Reesen.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.repository.RouteRepository;
import com.reesen.Reesen.service.interfaces.IRouteService;
import org.springframework.stereotype.Service;


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

}
