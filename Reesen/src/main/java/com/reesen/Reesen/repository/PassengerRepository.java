package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    
    Passenger findByEmail(String email);

    @Query("select pas.isConfirmedMail from Passenger pas where pas.email=:username")
    boolean getEmailConfirmation(String username);
}
