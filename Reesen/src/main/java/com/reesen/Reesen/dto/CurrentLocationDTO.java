package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;

public class CurrentLocationDTO {

    private String address;
    private double latitude;
    private double longitude;

    public CurrentLocationDTO() {
    }

    public CurrentLocationDTO(Location location) {
        this.address = location.getAddress();
        this.latitude = location.getLongitude();
        this.longitude = location.getLongitude();
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
