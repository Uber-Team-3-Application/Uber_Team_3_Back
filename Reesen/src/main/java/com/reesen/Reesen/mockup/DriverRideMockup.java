package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DriverRideMockup {

    private  Long id;
    private  Date startTime;
    private  Date endTime;
    private  double totalCost;
    private UserDTO driver;
    private  Set<UserDTO> passengers;
    private  int estimatedTimeInMinutes;
    private  String vehicleType;
    private  boolean babyTransport;
    private  boolean petTransport;
    private  DeductionDTO rejection;
    private  Set<RouteDTO> locations;

    public DriverRideMockup(Long id){
        this.id = id;
        locations = new HashSet<>();
        passengers = new HashSet<>();

        startTime = Date.from(Instant.now());
        endTime = Date.from(Instant.now());
        LocationDTO departure = new LocationDTO( "Bulevar Oslobodjenja 74", 45.267136, 19.833549);
        LocationDTO destination = new LocationDTO( "Bulevar Oslobodjenja 74", 45.267136, 19.83354 );
        locations.add(new RouteDTO(id, departure, destination));


        rejection = new DeductionDTO(
                        "Ride is canceled due to previous problems with the passenger",
                        LocalDateTime.now());

        totalCost = 1235;
        driver = new UserDTO(Long.parseLong("123"), "user@example.com");

        UserDTO passenger = new UserDTO(Long.parseLong("123"), "user@example.com");
        passengers.add(passenger);
        estimatedTimeInMinutes = 5;
        vehicleType = "STANDARDNO";
        babyTransport = true;
        petTransport = true;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<RouteDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<RouteDTO> locations) {
        this.locations = locations;
    }

    public DeductionDTO getRejection() {
        return rejection;
    }

    public void setRejection(DeductionDTO rejections) {
        this.rejection = rejections;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public UserDTO getDriver() {
        return driver;
    }

    public void setDriver(UserDTO driver) {
        this.driver = driver;
    }

    public Set<UserDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<UserDTO> passengers) {
        this.passengers = passengers;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
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
