package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Route findByEmail(String email);
}
