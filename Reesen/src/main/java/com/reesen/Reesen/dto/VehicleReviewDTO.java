package com.reesen.Reesen.dto;

import java.util.HashSet;

public class VehicleReviewDTO {

    private HashSet<ReviewWithPassengerDTO> vehicleReview;

    public VehicleReviewDTO(HashSet<ReviewWithPassengerDTO> vehicleReview) {
        this.vehicleReview = vehicleReview;
    }

    public HashSet<ReviewWithPassengerDTO> getVehicleReview() {
        return vehicleReview;
    }

    public void setVehicleReview(HashSet<ReviewWithPassengerDTO> vehicleReview) {
        this.vehicleReview = vehicleReview;
    }
}
