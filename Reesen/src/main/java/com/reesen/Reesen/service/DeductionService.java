package com.reesen.Reesen.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.reesen.Reesen.model.Deduction;
import com.reesen.Reesen.repository.DeductionRepository;
import com.reesen.Reesen.service.interfaces.IDeductionService;

public class DeductionService implements IDeductionService {
	
	private final DeductionRepository deductionRepository;
	
    @Autowired
    public DeductionService(DeductionRepository deductionRepository){
        this.deductionRepository = deductionRepository;
    }

    @Override
    public Deduction findOne(Long id){
        return this.deductionRepository.findById(id).orElseGet(null);
    }

    @Override
    public Deduction save(Deduction deduction) {
        return this.deductionRepository.save(deduction);
    }
}
