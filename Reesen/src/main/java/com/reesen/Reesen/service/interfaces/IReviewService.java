package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Review;

import java.util.Set;

public interface IReviewService {
	Review findOne(Long id);
	Review save(Review review);
	Set<Review> getReviews();
}
