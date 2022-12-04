package com.reesen.Reesen.dto;

import java.util.Date;

import com.reesen.Reesen.model.Deduction;

public class DeductionDTO {

	private Long id;
	private Long userId;
	private Long rideId;
	private String reason;
	private Date deductionTime;

	public DeductionDTO() {

	}

	public DeductionDTO(Deduction deduction) {
		this.id = deduction.getId();
		this.userId = deduction.getUser().getId();
		this.rideId = deduction.getRide().getId();
		this.reason = deduction.getReason();
		this.deductionTime = deduction.getDeductionTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRideId() {
		return rideId;
	}

	public void setRideId(Long rideId) {
		this.rideId = rideId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getDeductionTime() {
		return deductionTime;
	}

	public void setDeductionTime(Date deductionTime) {
		this.deductionTime = deductionTime;
	}


}