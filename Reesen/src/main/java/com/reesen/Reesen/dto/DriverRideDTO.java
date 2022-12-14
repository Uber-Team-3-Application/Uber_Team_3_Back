package com.reesen.Reesen.dto;

import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Route;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DriverRideDTO {
    private  Long id;
    private Date startTime;
    private  Date endTime;
    private  double totalCost;
    private UserDTO driver;
    private Set<UserDTO> passengers;
    private  double estimatedTimeInMinutes;
    private  String vehicleType;
    private  boolean babyTransport;
    private  boolean petTransport;
    private  DeductionDTO rejection;
    private  Set<RouteDTO> locations;

    public DriverRideDTO() {
    }

    public DriverRideDTO(Ride ride) {
        this.id = ride.getId();
        this.startTime = ride.getTimeOfStart();
        this.endTime = ride.getTimeOfEnd();
        this.totalCost = ride.getTotalPrice();
        this.estimatedTimeInMinutes = ride.getEstimatedTime();
        this.babyTransport = ride.isBabyAccessible();
        this.petTransport = ride.isPetAccessible();
        this.vehicleType = ride.getVehicleType().toString();
        this.driver = new UserDTO(ride.getDriver().getId(), ride.getDriver().getEmail());
        setPassengers(ride);
        this.rejection = new DeductionDTO(ride.getDeduction().getReason(), ride.getDeduction().getDeductionTime());
        this.locations = setLocations(ride);

    }

    private void setPassengers(Ride ride) {
        for (Passenger passenger : ride.getPassengers()) {
            passengers.add(new UserDTO(passenger.getId(), passenger.getEmail()));
        }
    }
    private Set<RouteDTO> setLocations(Ride ride){

        Set<RouteDTO> routes = new HashSet<>();
        for(Route route: ride.getLocations()){
            LocationDTO departure = new LocationDTO(route.getDeparture());
            LocationDTO destination = new LocationDTO(route.getDestination());
            routes.add(new RouteDTO(departure, destination));
        }
        return routes;

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DeductionDTO getRejection() {
        return rejection;
    }

    public void setRejection(DeductionDTO rejection) {
        this.rejection = rejection;
    }

    public Set<RouteDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<RouteDTO> locations) {
        this.locations = locations;
    }
}
