package com.reesen.Reesen.dto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public class CreateRideDTO {

    private Set<UserDTO> passengers;
    private LinkedHashSet<RouteDTO> locations;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private LocalDateTime scheduledTime;

    public CreateRideDTO() {
        super();
    }
    public LinkedHashSet<RouteDTO> getLocations() {
        return locations;
    }
    public void setLocations(LinkedHashSet<RouteDTO> locations) {
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

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
