package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Review;
import java.util.Set;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Set<Review> findReviewsByRide(Ride ride);


    @Query("select r from Review r where r.ride.id=:rideId")
    Set<Review> findAllByRideId(Long rideId);

    @Query("select r.passenger from Review r where r.id=:reviewID")
    Optional<Passenger> findPassengerByReviewId(Long reviewID);
}
