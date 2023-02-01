package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentLocationDTO {

    private String address;
    private double latitude;
    private double longitude;

    public CurrentLocationDTO(Location location) {
        this.address = location.getAddress();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

}
