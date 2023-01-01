package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Deduction;
import com.reesen.Reesen.model.Ride;
import java.util.Optional;


public interface IDeductionService {
	Deduction findOne(Long id);
	Deduction save(Deduction deduction);
	Optional<Deduction> findDeductionByRide(Ride ride);

}
