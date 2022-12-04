package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Path;

public class PathDTO {
	
	private Long id;
	private Long routeId;
	private double lengthInKm;
	
	public PathDTO() {

	}
	
	public PathDTO(Path path) {
		this.id = path.getId();
		this.routeId = path.getRoute().getId();
		this.lengthInKm = path.getLengthInKm();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getRouteId() {
		return routeId;
	}
	
	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}
	
	public double getLengthInKm() {
		return lengthInKm;
	}
	
	public void setLengthInKm(double lengthInKm) {
		this.lengthInKm = lengthInKm;
	}
}
