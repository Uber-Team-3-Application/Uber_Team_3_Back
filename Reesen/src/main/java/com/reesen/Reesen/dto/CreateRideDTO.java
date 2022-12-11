package com.reesen.Reesen.dto;

import java.util.Set;

public class CreateRideDTO {

    private Set<UserDTO> passengers;
    private Set<RouteDTO> locations;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public CreateRideDTO() {
        super();
    }
    public Set<RouteDTO> getLocations() {
        return locations;
    }
    public void setLocations(Set<RouteDTO> locations) {
        this.locations = locations;
    }
    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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
    public Set<UserDTO> getPassengers() {
        return passengers;
    }
    public void setPassengers(Set<UserDTO> passengers) {
        this.passengers = passengers;
    }



}
