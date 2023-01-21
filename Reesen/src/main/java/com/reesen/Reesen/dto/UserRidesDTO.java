package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Route;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class UserRidesDTO {

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
    private Set<RideReviewDTO> reviews;

    public UserRidesDTO() {
    }

    public UserRidesDTO(Ride ride) {
        this.id = ride.getId();
        this.startTime = ride.getTimeOfStart();
        this.endTime = ride.getTimeOfEnd();
        this.totalCost = ride.getTotalPrice();
        this.estimatedTimeInMinutes = ride.getEstimatedTime();
        this.babyTransport = ride.isBabyAccessible();
        this.petTransport = ride.isPetAccessible();
        this.vehicleType = ride.getVehicleType().toString();
        this.rejection = new DeductionDTO(ride.getDeduction().getReason(), ride.getDeduction().getDeductionTime());
        this.locations = setLocations(ride);
        setReviews(ride);
        setPassengers(ride);

    }

    public Set<RideReviewDTO> getReviews() {
        return reviews;
    }
    public void setReviews(Ride ride) {
        this.reviews = new HashSet<>();
        for(Review review: ride.getReview()){
            ReviewWithPassengerDTO vehicleReview = new ReviewWithPassengerDTO();
            vehicleReview.setComment(review.getVehicleComment());
            vehicleReview.setRating(review.getVehicleRating());
            vehicleReview.setPassenger(new UserDTO(review.getPassenger()));

            ReviewWithPassengerDTO driverReview = new ReviewWithPassengerDTO();
            driverReview.setComment(review.getDriverComment());
            driverReview.setRating(review.getDriverRating());
            driverReview.setPassenger(new UserDTO(review.getPassenger()));

            reviews.add(new RideReviewDTO(vehicleReview, driverReview));
        }
    }
    public void setReviews(Set<RideReviewDTO> reviews) {
        this.reviews = reviews;
    }

    private void setPassengers(Ride ride) {
        this.passengers = new HashSet<UserDTO>();
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

    @Override
    public String toString() {
        return "UserRidesDTO{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalCost=" + totalCost +
                ", driver=" + driver +
                ", passengers=" + passengers +
                ", estimatedTimeInMinutes=" + estimatedTimeInMinutes +
                ", vehicleType='" + vehicleType + '\'' +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                ", rejection=" + rejection +
                ", locations=" + locations +
                ", reviews=" + reviews +
                '}';
    }
}
