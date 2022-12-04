package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Deduction;

public interface IDeductionService {
	Deduction findOne(Long id);
	Deduction save(Deduction deduction);
}
