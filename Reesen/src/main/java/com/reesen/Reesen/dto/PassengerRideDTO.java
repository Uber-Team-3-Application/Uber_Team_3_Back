package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Review;
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
public class PassengerRideDTO {
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


    public PassengerRideDTO(Ride ride) {
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


    public PassengerRideDTO newInstance(Ride ride) {
        this.id = ride.getId();
        this.startTime = ride.getTimeOfStart();
        this.endTime = ride.getTimeOfEnd();
        this.totalCost = ride.getTotalPrice();
        this.estimatedTimeInMinutes = ride.getEstimatedTime();
        this.babyTransport = ride.isBabyAccessible();
        this.petTransport = ride.isPetAccessible();
        this.vehicleType = ride.getVehicleType().toString();
        setPassengers(ride);
        if (ride.getDeduction() != null)
             this.rejection = new DeductionDTO(ride.getDeduction().getReason(), ride.getDeduction().getDeductionTime());
        else this.rejection = null;
        this.locations = setLocations(ride);
        return this;
    }

    private Set<RideReviewDTO> setRideReviews(Set<Review> reviews) {
        Set<RideReviewDTO> reviewDTOS = new HashSet<>();
        for(Review review: reviews){
            UserDTO passenger = new UserDTO(review.getPassenger().getId(), review.getPassenger().getEmail());
            ReviewWithPassengerDTO driverReview =
                    new ReviewWithPassengerDTO(review.getId(),
                            review.getDriverRating(),
                            review.getDriverComment(),
                            passenger
                    );
            ReviewWithPassengerDTO vehicleReview =
                    new ReviewWithPassengerDTO(review.getId(),
                            review.getVehicleRating(),
                            review.getVehicleComment(),
                            passenger
                    );
            reviewDTOS.add(new RideReviewDTO(vehicleReview, driverReview));
        }
        return reviewDTOS;
    }


    private void setPassengers(Ride ride) {
        Set<UserDTO> passengers = new HashSet<>();
        for (Passenger passenger : ride.getPassengers()) {
            passengers.add(new UserDTO(passenger.getId(), passenger.getEmail()));
        }
        this.passengers = passengers;
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
