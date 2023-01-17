package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Review;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ReviewDTO {

	@NotNull(message = "{required}")
	@Min(value = 1, message = "{regex}")
	@Max(value = 10, message = "{regex}")
	private int rating;
	@NotNull(message = "{required}")
	@Length(max = 500, message = "{regex}")
	private String comment;

	public ReviewDTO() {
	}

	public ReviewDTO(int rating, String comment) {
		this.rating = rating;
		this.comment = comment;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
