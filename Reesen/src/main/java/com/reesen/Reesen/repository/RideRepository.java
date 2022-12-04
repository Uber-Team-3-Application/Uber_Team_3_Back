package com.reesen.Reesen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Ride;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

}
