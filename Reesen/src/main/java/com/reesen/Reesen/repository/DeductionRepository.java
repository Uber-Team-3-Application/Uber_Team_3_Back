package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Deduction;

import java.util.Optional;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long>{

    @Query("select d from Deduction d where d.ride=:ride")
    Optional<Deduction> findDeductionWhereRideEquals(Ride ride);
}
