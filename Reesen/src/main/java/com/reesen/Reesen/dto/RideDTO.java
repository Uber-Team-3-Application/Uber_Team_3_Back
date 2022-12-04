package com.reesen.Reesen.dto;


import java.util.Date;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.VehicleType;

public class RideDTO {

    private Long id;
    private Date timeOfStart;
    private Date timeOfEnd;
    private double totalPrice;
    private Long driverId;
    private double estimatedTime;
    private RideStatus status;
    private boolean isPanicPressed;
    private boolean isBabyAccessible;
    private boolean isPetAccessible;
    private VehicleType vehicleType;

    public RideDTO(){

    }

	public RideDTO(Ride ride) {
		this.id = ride.getId();
		this.timeOfStart = ride.getTimeOfStart();
		this.timeOfEnd = ride.getTimeOfEnd();
		this.totalPrice = ride.getTotalPrice();
		this.driverId = ride.getDriver().getId();
		this.estimatedTime = ride.getEstimatedTime();
		this.status = ride.getStatus();
		this.isPanicPressed = ride.isPanicPressed();
		this.isBabyAccessible = ride.isBabyAccessible();
		this.isPetAccessible = ride.isPetAccessible();
		this.vehicleType = ride.getVehicleType();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeOfStart() {
		return timeOfStart;
	}

	public void setTimeOfStart(Date timeOfStart) {
		this.timeOfStart = timeOfStart;
	}

	public Date getTimeOfEnd() {
		return timeOfEnd;
	}

	public void setTimeOfEnd(Date timeOfEnd) {
		this.timeOfEnd = timeOfEnd;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public double getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public RideStatus getStatus() {
		return status;
	}

	public void setStatus(RideStatus status) {
		this.status = status;
	}

	public boolean isPanicPressed() {
		return isPanicPressed;
	}

	public void setPanicPressed(boolean isPanicPressed) {
		this.isPanicPressed = isPanicPressed;
	}

	public boolean isBabyAccessible() {
		return isBabyAccessible;
	}

	public void setBabyAccessible(boolean isBabyAccessible) {
		this.isBabyAccessible = isBabyAccessible;
	}

	public boolean isPetAccessible() {
		return isPetAccessible;
	}

	public void setPetAccessible(boolean isPetAccessible) {
		this.isPetAccessible = isPetAccessible;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

}
