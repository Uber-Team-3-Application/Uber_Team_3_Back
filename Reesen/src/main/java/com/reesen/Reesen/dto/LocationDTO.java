package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;

public class LocationDTO {
	private String address;
	private double latitude;
	private double longitude;
	
    public LocationDTO() {
    }

    public LocationDTO(Location location) {
        this.latitude = location.getGeographicWidth();
        this.longitude = location.getGeographicLength();
        this.address = location.getAddress();
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
