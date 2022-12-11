package com.reesen.Reesen.dto;

import java.util.Date;

<<<<<<< Updated upstream:Reesen/src/main/java/com/reesen/Reesen/dto/Ride/RideDTO.java
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
=======
import java.util.Set;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.model.*;

public class RideDTO {

	private Long id;
	private Set<RouteDTO> locations;
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
>>>>>>> Stashed changes:Reesen/src/main/java/com/reesen/Reesen/dto/RideDTO.java

    public RideDTO(){

    }

	public RideDTO(Ride ride) {
		this.id = ride.getId();
<<<<<<< Updated upstream:Reesen/src/main/java/com/reesen/Reesen/dto/Ride/RideDTO.java
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
=======
		this.startTime = ride.getTimeOfStart();
		this.endTime = ride.getTimeOfEnd();
		this.totalCost = ride.getTotalPrice();
		this.estimatedTimeInMinutes = ride.getEstimatedTime();
		this.babyTransport = ride.isBabyAccessible();
		this.petTransport = ride.isPetAccessible();
		this.status = ride.getStatus();
		setPassengers(ride);
		setVehicleType(ride);
		this.rejection = new DeductionDTO(ride.getDeduction().getReason(), ride.getDeduction().getDeductionTime());
	}


	private void setPassengers(Ride ride) {
		for (Passenger passenger : ride.getPassengers()) {
			passengers.add(new UserDTO(passenger.getId(), passenger.getEmail()));
		}
	}

	private void setVehicleType(Ride ride) {
		if (ride.getVehicleType().getName() == VehicleName.VAN)
			this.vehicleType = VehicleTypeDTO.KOMBI;
		else if (ride.getVehicleType().getName() == VehicleName.LUXURY)
			this.vehicleType = VehicleTypeDTO.LUKSUZNO;
		else if (ride.getVehicleType().getName() == VehicleName.STANDARD)
			this.vehicleType = VehicleTypeDTO.STANDARDNO;
	}

	public DeductionDTO getRejection() {
		return rejection;
	}

	public void setRejection(DeductionDTO rejection) {
		this.rejection = rejection;
>>>>>>> Stashed changes:Reesen/src/main/java/com/reesen/Reesen/dto/RideDTO.java
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

	public RideStatus getStatus() {
		return status;
	}

	public void setStatus(RideStatus status) {
		this.status = status;
	}
}
