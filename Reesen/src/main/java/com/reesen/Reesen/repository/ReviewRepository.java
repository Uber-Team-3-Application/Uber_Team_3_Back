package com.reesen.Reesen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Review;
import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query("select r from Review r where r.ride.id=:rideId")
    Set<Review> findAllByRideId(Long rideId);
}
