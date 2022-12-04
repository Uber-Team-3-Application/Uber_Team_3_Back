package com.reesen.Reesen.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.repository.ReviewRepository;
import com.reesen.Reesen.service.interfaces.IReviewService;

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

}
