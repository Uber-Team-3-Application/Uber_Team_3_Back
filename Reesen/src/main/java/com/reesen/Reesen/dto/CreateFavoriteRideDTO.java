package com.reesen.Reesen.dto;

import java.util.LinkedHashSet;
import java.util.Set;

public class CreateFavoriteRideDTO {
    private String favoriteName;
    private Set<UserDTO> passengers;
    private LinkedHashSet<RouteDTO> locations;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public CreateFavoriteRideDTO() {
    }

    public CreateFavoriteRideDTO(String favoriteName, Set<UserDTO> passengers, LinkedHashSet<RouteDTO> locations, String vehicleType, boolean babyTransport, boolean petTransport) {
        this.favoriteName = favoriteName;
        this.passengers = passengers;
        this.locations = locations;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public Set<UserDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<UserDTO> passengers) {
        this.passengers = passengers;
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
}
