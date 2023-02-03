package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PanicLocationDTO {

    private String address;
    private double latitude;
    private double longitude;


    public PanicLocationDTO(Location location) {
        this.address = location.getAddress();
        this.latitude = location.getLongitude();
        this.longitude = location.getLongitude();
    }

}
