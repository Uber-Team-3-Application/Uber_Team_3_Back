package com.reesen.Reesen.mockup;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.dto.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class RideMockup {
    private  Long id;
    private LinkedHashSet<RouteDTO> locations;
    private  DeductionDTO rejection;
    private Date startTime;
    private  Date endTime;
    private  double totalCost;
    private UserDTO driver;
    private  Set<UserDTO> passengers;
    private  int estimatedTimeInMinutes;
    private  String vehicleType;
    private  boolean babyTransport;
    private  boolean petTransport;
    private RideStatus status;

    public RideMockup(){
        id = Long.parseLong("10");
        locations = new LinkedHashSet<>();
        passengers = new HashSet<>();
        rejection = (
                new DeductionDTO(
                        "Ride is canceled due to previous problems with the passenger",
                        LocalDateTime.now()

                ));
        locations.add(
                new RouteDTO(
                        id, new LocationDTO( "Kuca Poso", 45.267136, 19.833549),
                        new LocationDTO( "Poso Kuca", 45.267136, 19.833549)
                ));
        startTime = Date.from(Instant.now());
        endTime = Date.from(Instant.now());
        totalCost = 1235;
        driver = new UserDTO("user@example.com");
        driver.setId(Long.parseLong("123"));

        UserDTO passenger = new UserDTO(Long.parseLong("123"), "user@example.com");
        passengers.add(passenger);
        estimatedTimeInMinutes = 5;
        vehicleType = "STANDARDNO";
        babyTransport = true;
        petTransport = true;
        status = RideStatus.ACTIVE;
    }

    public RideDTO getRide(){
        RideDTO ride = new RideDTO();
        ride.setId(id);
        ride.setDriver(driver);
        ride.setLocations(locations);
        ride.setPassengers(passengers);
        ride.setEndTime(endTime);
        ride.setRejection(rejection);
        ride.setBabyTransport(babyTransport);
        ride.setStartTime(startTime);
        ride.setEstimatedTimeInMinutes(estimatedTimeInMinutes);
        ride.setVehicleType(VehicleTypeDTO.valueOf(vehicleType));
        ride.setStatus(status);
        ride.setPetTransport(petTransport);
        ride.setTotalCost(totalCost);
        return ride;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LinkedHashSet<RouteDTO> getLocations() {
        return locations;
    }

    public void setLocations(LinkedHashSet<RouteDTO> locations) {
        this.locations = locations;
    }

    public DeductionDTO getRejection() {
        return rejection;
    }

    public void setRejection(DeductionDTO rejection) {
        this.rejection = rejection;
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

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }
}
