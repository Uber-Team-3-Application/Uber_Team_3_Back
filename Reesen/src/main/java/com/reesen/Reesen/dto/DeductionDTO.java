package com.reesen.Reesen.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.reesen.Reesen.model.Deduction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DeductionDTO {

	private String reason;
	private LocalDateTime timeOfRejection;

	public DeductionDTO(String reason, LocalDateTime timeOfRejection){
		this.reason = reason;
		this.timeOfRejection = timeOfRejection;
	}
	public DeductionDTO(Deduction deduction) {
		this.reason = deduction.getReason();
		this.timeOfRejection = deduction.getDeductionTime();
	}



}

