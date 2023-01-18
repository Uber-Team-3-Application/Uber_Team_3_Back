package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import org.springframework.beans.factory.annotation.Autowired;

import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.repository.ReviewRepository;
import com.reesen.Reesen.service.interfaces.IReviewService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ReviewService implements IReviewService {
	
	private final ReviewRepository reviewRepository;
	
    @Autowired
    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

	@Override
	public Review findOne(Long id) {
		return this.reviewRepository.findById(id).orElseGet(null);
	}

	@Override
	public Review save(Review review) {
		return  this.reviewRepository.save(review);
	}

	@Override
	public Set<Review> findReviewsByRide(Ride ride) {
		return this.reviewRepository.findReviewsByRide(ride);
	}

	@Override
	public Set<Review> getReviews() {return new HashSet<>(this.reviewRepository.findAll());}

	@Override
	public Optional<Passenger> findPassengerByReviewId(Long reviewID) {
		return this.reviewRepository.findPassengerByReviewId(reviewID);
	}

	@Override
	public Optional<Review> findReviewByPassengerAndRide(Passenger passenger, Ride ride) {
		return this.reviewRepository.findReviewByPassengerAndRide(passenger,ride);
	}

}
