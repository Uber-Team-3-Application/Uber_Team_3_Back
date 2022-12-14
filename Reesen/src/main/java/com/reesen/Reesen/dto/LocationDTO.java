package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;

public class LocationDTO {

    private String address;
	private double latitude;
	private double longitude;
	
    public LocationDTO() {
    }

    public LocationDTO(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationDTO(Location location) {

        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
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


}
