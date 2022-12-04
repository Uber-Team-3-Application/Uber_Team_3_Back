package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Route;

public interface IRouteService {
	Route findOne(Long id);
	Route save(Route route);
}
