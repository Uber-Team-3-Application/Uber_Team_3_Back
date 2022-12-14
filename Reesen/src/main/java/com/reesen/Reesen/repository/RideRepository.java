package com.reesen.Reesen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Ride;
import java.util.Date;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

}