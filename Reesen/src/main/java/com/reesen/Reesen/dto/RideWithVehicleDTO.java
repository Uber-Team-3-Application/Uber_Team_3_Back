package com.reesen.Reesen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideWithVehicleDTO {
    private Long rideId;
    private Long vehicleId;
    private double latitude;
    private double longitude;
    private String address;

}
