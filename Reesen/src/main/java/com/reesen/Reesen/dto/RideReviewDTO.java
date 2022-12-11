package com.reesen.Reesen.dto;

public class RideReviewDTO {

    private ReviewWithPassengerDTO vehicleReview;
    private ReviewWithPassengerDTO driverReview;

    public RideReviewDTO(ReviewWithPassengerDTO vehicleReview, ReviewWithPassengerDTO driverReview) {
        this.vehicleReview = vehicleReview;
        this.driverReview = driverReview;
    }

    public ReviewWithPassengerDTO getVehicleReview() {
        return vehicleReview;
    }

    public void setVehicleReview(ReviewWithPassengerDTO vehicleReview) {
        this.vehicleReview = vehicleReview;
    }

    public ReviewWithPassengerDTO getDriverReview() {
        return driverReview;
    }

    public void setDriverReview(ReviewWithPassengerDTO driverReview) {
        this.driverReview = driverReview;
    }
}
