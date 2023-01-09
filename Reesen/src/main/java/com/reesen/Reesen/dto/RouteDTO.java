package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Route;

public class RouteDTO {

	private Long id;
    private LocationDTO departure;
    private LocationDTO destination;
    
	public RouteDTO() {

	}

	public RouteDTO(Route route) {
		this.id = route.getId();
		this.departure = new LocationDTO(route.getDeparture());
		this.destination = new LocationDTO(route.getDestination());
	}

	public RouteDTO(Long id, LocationDTO departure, LocationDTO destination) {
		this.id = id;
		this.departure = departure;
		this.destination = destination;
	}
	public RouteDTO(LocationDTO departure, LocationDTO destination) {
		this.departure = departure;
		this.destination = destination;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocationDTO getDeparture() {
		return departure;
	}

	public void setDeparture(LocationDTO departure) {
		this.departure = departure;
	}

	public LocationDTO getDestination() {
		return destination;
	}

	public void setDestination(LocationDTO destination) {
		this.destination = destination;
	}
}
