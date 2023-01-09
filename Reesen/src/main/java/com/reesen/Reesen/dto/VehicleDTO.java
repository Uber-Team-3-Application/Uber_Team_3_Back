package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Vehicle;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class VehicleDTO {

    private Long id;
    private Long driverId;
    private String vehicleType;
    private String model;
    private String licenseNumber;
    private CurrentLocationDTO currentLocation;
    private int passengerSeats;
    private boolean babyTransport;
    private boolean petTransport;

    public VehicleDTO(Vehicle vehicle){
        this.id = vehicle.getId();
        this.driverId = vehicle.getDriver().getId();
        this.model = vehicle.getModel();
        this.vehicleType = vehicle.getType().getName().toString();
        this.licenseNumber = vehicle.getRegistrationPlate();
        this.currentLocation = new CurrentLocationDTO(vehicle.getCurrentLocation());
        this.passengerSeats = vehicle.getPassengerSeats();
        this.babyTransport = vehicle.isBabyAccessible();
        this.petTransport = vehicle.isPetAccessible();

    }
}
