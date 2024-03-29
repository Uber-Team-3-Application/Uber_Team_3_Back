package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleLocationSimulationDTO {

    private Long id;
    private String licensePlateNumber;
    private double latitude;
    private double longitude;

    public VehicleLocationSimulationDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.licensePlateNumber = vehicle.getRegistrationPlate();
        this.latitude = vehicle.getCurrentLocation().getLatitude();
        this.longitude = vehicle.getCurrentLocation().getLongitude();
    }

}
