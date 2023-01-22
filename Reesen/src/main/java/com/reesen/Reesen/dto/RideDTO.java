package com.reesen.Reesen.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

	public RideDTO(Ride ride) {
		this.id = ride.getId();
		this.startTime = ride.getTimeOfStart();
		this.endTime = ride.getTimeOfEnd();
		this.totalCost = ride.getTotalPrice();
		this.estimatedTimeInMinutes = ride.getEstimatedTime();
		this.babyTransport = ride.isBabyAccessible();
		this.petTransport = ride.isPetAccessible();
		this.status = ride.getStatus();
		this.driver = new UserDTO(ride.getDriver().getId(), ride.getDriver().getEmail());
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


}
