package com.reesen.Reesen.dto;

import java.util.Date;

import com.reesen.Reesen.model.Deduction;

public class DeductionDTO {

	private String reason;
	private Date timeOfRejection;

	public DeductionDTO() {

	}

	public DeductionDTO(String reason, Date timeOfRejection) {
		this.reason = reason;
		this.timeOfRejection = timeOfRejection;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getTimeOfRejection() {
		return timeOfRejection;
	}

	public void setTimeOfRejection(Date timeOfRejection) {
		this.timeOfRejection = timeOfRejection;
	}


}

