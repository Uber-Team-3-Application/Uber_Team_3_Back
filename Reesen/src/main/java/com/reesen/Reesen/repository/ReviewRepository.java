package com.reesen.Reesen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
