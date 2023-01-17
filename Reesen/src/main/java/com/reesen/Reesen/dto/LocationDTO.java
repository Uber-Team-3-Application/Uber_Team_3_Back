package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LocationDTO {

    private String address;
    @NotNull(message = "{required}")
    @Min(value = -90, message = "{regex}")
    @Max(value = 90, message = "{regex}")
    private double latitude;
    @NotNull(message = "{required}")
    @Min(value = -180, message = "{regex}")
    @Max(value = 180, message = "{regex}")
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
