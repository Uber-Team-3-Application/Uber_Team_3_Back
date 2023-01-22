package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewWithPassengerDTO {
    private Long id;
    private int rating;
    private String comment;
    private UserDTO passenger;


    public ReviewWithPassengerDTO(Review review, boolean isDriverComment) {
        this.id = review.getId();
        this.rating = isDriverComment ? review.getDriverRating() : review.getVehicleRating();
        this.comment = isDriverComment ? review.getDriverComment() : review.getVehicleComment();
        this.passenger = new UserDTO(review.getPassenger().getId(), review.getPassenger().getEmail());
    }

}
