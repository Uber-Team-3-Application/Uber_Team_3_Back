package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;

public class LocationDTO {
	private Long id;
	private double geographicWidth;
	private double geographicLength;
	
    public LocationDTO() {
    }

    public LocationDTO(Location location) {
    	this.id = location.getId();
        this.geographicWidth = location.getGeographicWidth();
        this.geographicLength = location.getGeographicLength();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getGeographicWidth() {
        return geographicWidth;
    }

    public void setGeographicWidth(double geographicWidth) {
        this.geographicWidth = geographicWidth;
    }

    public double getGeographicLength() {
        return geographicLength;
    }

    public void setGeographicLength(double geographicLength) {
        this.geographicLength = geographicLength;
    }
}
