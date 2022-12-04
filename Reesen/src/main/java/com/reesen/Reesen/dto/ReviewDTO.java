package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Review;

public class ReviewDTO {
	
	private Long id;
	private int driverRating;
	private int vehicleRating;
	private String comment;
	private Long passengerId;
	private Long rideId;
	
	public ReviewDTO() {
		
	}

	public ReviewDTO(Review review) {
		this.id = review.getId();
		this.driverRating = review.getDriverRating();
		this.vehicleRating = review.getVehicleRating();
		this.comment = review.getComment();
		this.passengerId = review.getPassenger().getId();
		this.rideId = review.getRide().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDriverRating() {
		return driverRating;
	}

	public void setDriverRating(int driverRating) {
		this.driverRating = driverRating;
	}

	public int getVehicleRating() {
		return vehicleRating;
	}

	public void setVehicleRating(int vehicleRating) {
		this.vehicleRating = vehicleRating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(Long passengerId) {
		this.passengerId = passengerId;
	}

	public Long getRideId() {
		return rideId;
	}

	public void setRideId(Long rideId) {
		this.rideId = rideId;
	}

}
