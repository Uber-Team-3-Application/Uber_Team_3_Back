package com.reesen.Reesen.model;

import com.reesen.Reesen.dto.RouteDTO;
import com.reesen.Reesen.dto.UserDTO;

import java.util.LinkedHashSet;
import java.util.Set;

public class FavoriteRideDTO {

    private Long id;
    private String favoriteName;
    private Set<UserDTO> passengers;
    private LinkedHashSet<RouteDTO> locations;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public FavoriteRideDTO() {
    }

    public FavoriteRideDTO(String favoriteName, Set<UserDTO> passengers, LinkedHashSet<RouteDTO> locations, String vehicleType, boolean babyTransport, boolean petTransport) {
        this.favoriteName = favoriteName;
        this.passengers = passengers;
        this.locations = locations;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public FavoriteRideDTO(Long id, String favoriteName, Set<UserDTO> passengers, LinkedHashSet<RouteDTO> locations, String vehicleType, boolean babyTransport, boolean petTransport) {
        this.id = id;
        this.favoriteName = favoriteName;
        this.passengers = passengers;
        this.locations = locations;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
