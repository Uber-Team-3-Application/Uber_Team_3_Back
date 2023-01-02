package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Deduction;

import java.util.Optional;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long>{
    public Optional<Deduction> findDeductionByRide(Ride ride);
}
