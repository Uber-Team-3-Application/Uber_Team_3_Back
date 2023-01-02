package com.reesen.Reesen.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.reesen.Reesen.model.Deduction;

public class DeductionDTO {

	private String reason;
	private LocalDateTime timeOfRejection;

	public DeductionDTO() {

	}
	public DeductionDTO(String reason, LocalDateTime timeOfRejection){
		this.reason = reason;
		this.timeOfRejection = timeOfRejection;
	}
	public DeductionDTO(Deduction deduction) {
		this.reason = deduction.getReason();
		this.timeOfRejection = deduction.getDeductionTime();
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getTimeOfRejection() {
		return this.timeOfRejection;
	}

	public void setTimeOfRejection(LocalDateTime deductionTime) {
		this.timeOfRejection = deductionTime;
	}


}

