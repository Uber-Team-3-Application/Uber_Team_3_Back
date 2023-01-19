package com.reesen.Reesen.model;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
public class FavoriteRide implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String favoriteName;

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Passenger> passengers;

    @Column
    private boolean isBabyAccessible;

    @Column
    private boolean isPetAccessible;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private VehicleType vehicleType;

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Route> locations;

    public FavoriteRide(){

    }

    public FavoriteRide(String name, Set<Passenger> passengers, boolean isBabyAccessible, boolean isPetAccessible, VehicleType vehicleType, Set<Route> locations) {
        this.favoriteName = name;
        this.passengers = passengers;
        this.isBabyAccessible = isBabyAccessible;
        this.isPetAccessible = isPetAccessible;
        this.vehicleType = vehicleType;
        this.locations = locations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public boolean isBabyAccessible() {
        return isBabyAccessible;
    }

    public void setBabyAccessible(boolean babyAccessible) {
        isBabyAccessible = babyAccessible;
    }

    public boolean isPetAccessible() {
        return isPetAccessible;
    }

    public void setPetAccessible(boolean petAccessible) {
        isPetAccessible = petAccessible;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Set<Route> getLocations() {
        return locations;
    }

    public void setLocations(Set<Route> locations) {
        this.locations = locations;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }
}

