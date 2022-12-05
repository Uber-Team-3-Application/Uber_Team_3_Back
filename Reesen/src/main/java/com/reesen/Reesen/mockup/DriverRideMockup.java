package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.DeductionDTO;
import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.dto.UserDTO;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.VehicleType;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DriverRideMockup {

    private  Long id;
    private  Set<Location> locations;
    private  Set<DeductionDTO> rejections;
    private  Date startTime;
    private  Date endTime;
    private  double totalCost;
    private UserDTO driver;
    private  Set<UserDTO> passengers;
    private  int estimatedTimeInMinutes;
    private  String vehicleType;
    private  boolean babyTransport;
    private  boolean petTransport;

    public DriverRideMockup(){
        id = Long.parseLong("10");
        locations = new HashSet<>();
        passengers = new HashSet<>();
        rejections = new HashSet<>();
        locations.add(new Location("Kuca Poso", 45.267136, 19.833549));
        rejections.add(
                new DeductionDTO(
                        "Ride is canceled due to previous problems with the passenger",
                        Date.from(Instant.now())

        ));
        startTime = Date.from(Instant.now());
        endTime = Date.from(Instant.now());
        totalCost = 1235;
        driver = new UserDTO("user@example.com", "VOZAC");
        driver.setId(Long.parseLong("123"));

        UserDTO passenger = new UserDTO(Long.parseLong("123"), "user@example.com", "PUTNIK");
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

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<DeductionDTO> getRejections() {
        return rejections;
    }

    public void setRejections(Set<DeductionDTO> rejections) {
        this.rejections = rejections;
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
