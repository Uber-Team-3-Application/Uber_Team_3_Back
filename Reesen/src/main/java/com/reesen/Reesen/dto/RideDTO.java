package com.reesen.Reesen.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.model.*;

public class RideDTO {

	private Long id;
	private LinkedHashSet<RouteDTO> locations;
	private DeductionDTO rejection;
 	private Date startTime;
 	private Date endTime;
 	private double totalCost;
 	private UserDTO driver;
 	private Set<UserDTO> passengers;
 	private double estimatedTimeInMinutes;
 	private VehicleTypeDTO vehicleType;
 	private boolean babyTransport;
 	private boolean petTransport;
 	private RideStatus status;
	 private LocalDateTime scheduledTime;

    public RideDTO(){

    }

	public RideDTO(Ride ride) {
		this.id = ride.getId();
		this.startTime = ride.getTimeOfStart();
		this.endTime = ride.getTimeOfEnd();
		this.totalCost = ride.getTotalPrice();
		this.estimatedTimeInMinutes = ride.getEstimatedTime();
		this.babyTransport = ride.isBabyAccessible();
		this.petTransport = ride.isPetAccessible();
		this.status = ride.getStatus();
		if(ride.getDriver() != null) {
			this.driver = new UserDTO(ride.getDriver().getId(), ride.getDriver().getEmail());
		}
		setPassengers(ride);
		setVehicleType(ride);
		setLocations(ride);
		if(ride.getDeduction() == null) this.rejection = null;
		else this.rejection = new DeductionDTO(ride.getDeduction().getReason(), ride.getDeduction().getDeductionTime());
		this.scheduledTime = ride.getScheduledTime();
	}

	private void setLocations(Ride ride) {
		locations = new LinkedHashSet<>();
		for (Route route : ride.getLocations()) {
			locations.add(new RouteDTO(route));
		}
	}


	private void setPassengers(Ride ride) {
		passengers = new HashSet<>();
		for (Passenger passenger : ride.getPassengers()) {
			passengers.add(new UserDTO(passenger.getId(), passenger.getEmail()));
		}
	}

	private void setVehicleType(Ride ride) {
		if (ride.getVehicleType().getName() == VehicleName.VAN)
			this.vehicleType = VehicleTypeDTO.VAN;
		else if (ride.getVehicleType().getName() == VehicleName.LUXURY)
			this.vehicleType = VehicleTypeDTO.LUXURY;
		else if (ride.getVehicleType().getName() == VehicleName.STANDARD)
			this.vehicleType = VehicleTypeDTO.STANDARD;
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

	public LinkedHashSet<RouteDTO> getLocations() {
		return locations;
	}

	public void setLocations(LinkedHashSet<RouteDTO> locations) {
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

	public VehicleTypeDTO getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleTypeDTO vehicleType) {
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

	public LocalDateTime getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(LocalDateTime scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
}
