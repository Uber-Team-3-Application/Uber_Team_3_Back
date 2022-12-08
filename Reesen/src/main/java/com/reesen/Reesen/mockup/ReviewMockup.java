package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.ReviewWithPassengerDTO;
import com.reesen.Reesen.dto.UserDTO;

public class ReviewMockup {
    public static ReviewWithPassengerDTO getReview() {
        ReviewWithPassengerDTO review = new ReviewWithPassengerDTO();
        review.setId(Long.parseLong("123"));
        review.setRating(3);
        review.setComment("The driver was driving really fast");
        review.setPassenger(new UserDTO(Long.parseLong("123"), "user@example.com"));
        return review;
    }
}
