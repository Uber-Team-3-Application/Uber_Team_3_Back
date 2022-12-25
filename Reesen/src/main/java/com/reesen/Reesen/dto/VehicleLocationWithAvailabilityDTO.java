package com.reesen.Reesen.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleLocationWithAvailabilityDTO {
    private boolean isAvailable;
    private String address;
    private double latitude;
    private double longitude;

    public VehicleLocationWithAvailabilityDTO(boolean isAvailable, String address, double latitude, double longitude) {
        this.isAvailable = isAvailable;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
