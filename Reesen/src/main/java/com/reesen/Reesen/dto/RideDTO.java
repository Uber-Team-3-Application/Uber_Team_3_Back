package com.reesen.Reesen.dto;


import java.util.Date;

import java.util.Date;
import java.util.Set;

import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.VehicleType;

public class RideDTO {

	private Long id;
	private Set<LocationDTO> locations; //
 	private DeductionDTO rejection;     //
 	private Date startTime;
 	private Date endTime;
 	private double totalCost;
 	private UserDTO driver;
 	private Set<UserDTO> passengers;   //
 	private double estimatedTimeInMinutes;
 	private VehicleType vehicleType;
 	private boolean babyTransport;
 	private boolean petTransport;


	public RideDTO(Ride ride) {
		this.id = ride.getId();
		this.startTime = ride.getTimeOfStart();
		this.endTime = ride.getTimeOfEnd();
		this.totalCost = ride.getTotalPrice();
		this.estimatedTimeInMinutes = ride.getEstimatedTime();
		this.vehicleType = ride.getVehicleType();
		this.babyTransport = ride.isBabyAccessible();
		this.petTransport = ride.isPetAccessible();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<LocationDTO> getLocations() {
		return locations;
	}

	public void setLocations(Set<LocationDTO> locations) {
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

	public double getEstimatedTimeInMinutes() {
		return estimatedTimeInMinutes;
	}

	public void setEstimatedTimeInMinutes(double estimatedTimeInMinutes) {
		this.estimatedTimeInMinutes = estimatedTimeInMinutes;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
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


