package com.reesen.Reesen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RideReviewDTO {

    private ReviewWithPassengerDTO vehicleReview;
    private ReviewWithPassengerDTO driverReview;


}
