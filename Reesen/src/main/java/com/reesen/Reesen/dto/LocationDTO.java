package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;

public class LocationDTO {
	private Long id;
    private String address;
	private double latitude;
	private double longitude;
	
    public LocationDTO() {
    }

    public LocationDTO(Location location) {
    	this.id = location.getId();
        this.address = location.getAddress();
        this.latitude = location.getLongitude();
        this.longitude = location.getLongitude();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
