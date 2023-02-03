package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Route;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class DriverRideDTO {
    private  Long id;
    private Date startTime;
    private  Date endTime;
    private  double totalCost;
    private UserFullDTO driver;
    private Set<UserFullDTO> passengers;
    private  double estimatedTimeInMinutes;
    private  String vehicleType;
    private  boolean babyTransport;
    private  boolean petTransport;
    private  DeductionDTO rejection;
    private  Set<RouteDTO> locations;

    public DriverRideDTO(Ride ride) {
        this.id = ride.getId();
        this.startTime = ride.getTimeOfStart();
        this.endTime = ride.getTimeOfEnd();
        this.totalCost = ride.getTotalPrice();
        this.estimatedTimeInMinutes = ride.getEstimatedTime();
        this.babyTransport = ride.isBabyAccessible();
        this.petTransport = ride.isPetAccessible();
        this.vehicleType = ride.getVehicleType().toString();
        this.driver = new UserFullDTO(ride.getDriver());
        setPassengers(ride);
        this.rejection = new DeductionDTO(ride.getDeduction().getReason(), ride.getDeduction().getDeductionTime());
        this.locations = setLocations(ride);

    }

    private void setPassengers(Ride ride) {
        this.passengers = new HashSet<UserFullDTO>();
        for (Passenger passenger : ride.getPassengers()) {
            passengers.add(new UserFullDTO(passenger));
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
}
