package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.model.User;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
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

}
