package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Route;

public class RouteDTO {
	
    private Long id;
    private Long startLocationId;
    private Long endLocationId;
    private double distanceKm;
    
	public RouteDTO() {

	}
	
	public RouteDTO(Route route) {
		this.id = route.getId();
		this.startLocationId = route.getStartLocation().getId();
		this.endLocationId = route.getEndLocation().getId();
		this.distanceKm = route.getDistanceKm();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStartLocationId() {
		return startLocationId;
	}

	public void setStartLocationId(Long startLocationId) {
		this.startLocationId = startLocationId;
	}

	public Long getEndLocationId() {
		return endLocationId;
	}

	public void setEndLocationId(Long endLocationId) {
		this.endLocationId = endLocationId;
	}

	public double getDistanceKm() {
		return distanceKm;
	}

	public void setDistanceKm(double distanceKm) {
		this.distanceKm = distanceKm;
	}    
}
