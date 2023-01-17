package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.model.Ride;

import java.util.Optional;
import java.util.Set;

public interface IReviewService {
	Review findOne(Long id);
	Review save(Review review);
	Set<Review> findReviewsByRide(Ride ride);
	Set<Review> getReviews();
	Optional<Passenger> findPassengerByReviewId(Long reviewID);
	Optional<Review> findReviewByPassengerAndRide(Passenger passenger, Ride ride);


}
