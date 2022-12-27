package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Vehicle;

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

    public VehicleDTO(){

    }

    public VehicleDTO(Vehicle vehicle){
        this.id = vehicle.getId();
        this.driverId = vehicle.getDriver().getId();
        if(vehicle.getType() != null)
            this.vehicleType = vehicle.getType().getName().toString();
        else
            this.vehicleType = VehicleTypeDTO.STANDARDNO.toString();
        this.model = vehicle.getModel();
        this.licenseNumber = vehicle.getRegistrationPlate();
        this.currentLocation = new CurrentLocationDTO(vehicle.getCurrentLocation());
        this.passengerSeats = vehicle.getPassengerSeats();
        this.babyTransport = vehicle.isBabyAccessible();
        this.petTransport = vehicle.isPetAccessible();

    }

    public CurrentLocationDTO getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(CurrentLocationDTO currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }



    public int getPassengerSeats() {
        return passengerSeats;
    }

    public void setPassengerSeats(int passengerSeats) {
        this.passengerSeats = passengerSeats;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }
}
