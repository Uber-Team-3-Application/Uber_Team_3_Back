package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Review;

public interface IReviewService {
	Review findOne(Long id);
	Review save(Review review);
}
