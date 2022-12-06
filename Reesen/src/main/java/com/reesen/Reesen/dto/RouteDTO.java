package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.model.Location;

public class RouteDTO {
	
    private Long id;
    private Location departure;
    private Location destination;
    
	public RouteDTO() {

	}

	public RouteDTO(Location departure, Location destination) {
		this.departure = departure;
		this.destination = destination;
	}

	public RouteDTO(Route route) {
		this.id = route.getId();
		this.departure = route.getDeparture();
		this.destination = route.getDestination();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Location getDeparture() {
		return departure;
	}

	public void setDeparture(Location startLocationId) {
		this.departure = startLocationId;
	}

	public Location getDestination() { return destination;}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

}
