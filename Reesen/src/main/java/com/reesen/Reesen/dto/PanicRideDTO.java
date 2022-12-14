package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.model.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PanicRideDTO {

    private Long id;
    private Date startTime;
    private Date endTime;
    private double totalCost;
    private UserDTO driver;
    private Set<UserDTO> passengers;
    private double estimatedTimeInMinutes;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private DeductionDTO rejection;
    private Set<RouteDTO> locations;

    public PanicRideDTO(){
        this.passengers = new HashSet<>();
        this.locations = new HashSet<>();
    }
    public PanicRideDTO(Ride ride){
        this.id = ride.getId();
        this.startTime = ride.getTimeOfStart();
        this.endTime = ride.getTimeOfEnd();
        this.totalCost = ride.getTotalPrice();
        this.driver = new UserDTO(ride.getDriver());
        this.passengers = new HashSet<>();
        for(Passenger pass:ride.getPassengers()){
            this.passengers.add(new UserDTO(pass));
        }
        this.estimatedTimeInMinutes = ride.getEstimatedTime();
        this.vehicleType = ride.getVehicleType().toString();
        this.babyTransport = ride.isBabyAccessible();
        this.petTransport = ride.isPetAccessible();
        this.locations = new HashSet<>();
        for(Route route:ride.getLocations()){
            this.locations.add(
                    new RouteDTO(new LocationDTO(route.getDeparture()), new LocationDTO(route.getDestination())));
        }
        this.rejection = new DeductionDTO(ride.getDeduction());
    }

    public void addPassenger(UserDTO passenger){
        this.passengers.add(passenger);
    }


    public void addLocation(RouteDTO location){
        this.locations.add(location);
    }
    public PanicRideDTO(Long id, Date startTime, Date endTime, double totalCost, UserDTO driver, Set<UserDTO> passengers, int estimatedTimeInMinutes, String vehicleType, boolean babyTransport, boolean petTransport
    , Set<RouteDTO> locations, DeductionDTO rejection) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.driver = driver;
        this.passengers = passengers;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.locations = locations;
        this.rejection = rejection;
    }

    public DeductionDTO getRejection() {
        return rejection;
    }

    public void setRejection(DeductionDTO rejection) {
        this.rejection = rejection;
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

    public double getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(double estimatedTimeInMinutes) {
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
