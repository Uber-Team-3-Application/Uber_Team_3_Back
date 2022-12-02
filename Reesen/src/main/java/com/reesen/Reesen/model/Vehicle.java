package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.REFRESH}, mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Driver driver;

    @Column(nullable = false)
    private String model;

    @Column
    private String registrationPlate;

    @Column
    private int numberOfSeats;

    @Column
    private boolean isBabyAccessible;

    @Column
    private boolean isPetAccessible;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "currentLocationId")
    private Location currentLocation;


    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicleTypeId")
    private VehicleType type;


    public Vehicle() {
    }

    public Vehicle(Driver driver, String model, String registrationPlate, int passengerSeats, boolean isBabyAccessible, boolean isPetAccessible, Location currentLocation, VehicleType type) {
        this.driver = driver;
        this.model = model;
        this.registrationPlate = registrationPlate;
        this.numberOfSeats = numberOfSeats;
        this.isBabyAccessible = isBabyAccessible;
        this.isPetAccessible = isPetAccessible;
        this.currentLocation = currentLocation;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public int getPassengerSeats() {
        return numberOfSeats;
    }

    public void setPassengerSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
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

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }
}
