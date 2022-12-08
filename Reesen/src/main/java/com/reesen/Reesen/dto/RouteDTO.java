package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.model.Location;

public class RouteDTO {
	
    private LocationDTO departure;
    private LocationDTO destination;
    
	public RouteDTO() {

	}

	public RouteDTO(LocationDTO departure, LocationDTO destination) {
		this.departure = departure;
		this.destination = destination;
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
