package com.reesen.Reesen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Deduction;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long>{

}
