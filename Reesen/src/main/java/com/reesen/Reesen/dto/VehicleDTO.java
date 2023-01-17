package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Vehicle;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class VehicleDTO {

    private Long id;
    private Long driverId;

    @NotEmpty(message = "{required}")
    @Length(max=25, message = "{maxLength}")
    private String vehicleType;

    @NotEmpty(message = "{required}")
    @Length(max=25, message = "{maxLength}")
    private String model;

    @NotEmpty(message = "{required}")
    @Length(max=10, message = "{maxLength}")
    private String licenseNumber;
    private CurrentLocationDTO currentLocation;

    @Min(1)
    private int passengerSeats;

    @NotNull
    private boolean babyTransport;

    @NotNull
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
