package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Route;
import com.reesen.Reesen.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Set;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    
    Passenger findByEmail(String email);

    @Query("select pas.isConfirmedMail from Passenger pas where pas.email=:username")
    Boolean getEmailConfirmation(String username);

    @Query("select d.password from Passenger d where d.id=:id")
    String getPasswordWithId(Long id);

    @Transactional
    @Modifying
    @Query("update Passenger p set p.isConfirmedMail=:true where p.id=:passengerId")
    void activateAccount(Long passengerId);

    Set<Passenger> findPassengersByRidesContaining(Ride ride);


    @Query("select p.rides from Passenger p where p.id=:passengerId")
    Page<Ride> findAllRidesByPassengerId(Long passengerId, Pageable page);

    @Query("select p.rides from Passenger p, Ride r  " +
            "where p.id=:passengerId " +
            "and " +
            "p member of r.passengers " +
            "and r.timeOfEnd<=:timeOfEnd and r.timeOfStart>=:timeOfStart")
    Page<Ride> findAllRidesByPassengerIdAndTimeOfStartAfterAndTimeOfEndBefore(
            Long passengerId,
            Date timeOfStart,
            Date timeOfEnd,
            Pageable page);

    @Query("select p.rides from Passenger p, Ride r  " +
            "where p.id=:passengerId " +
            "and " +
            "p member of r.passengers " +
            "and r.timeOfStart>=:timeOfStart")
    Page<Ride> findAllRidesByPassengerIdAndTimeOfStartAfter(
            Long passengerId,
            Date timeOfStart,
            Pageable page);


    @Query("select p.rides from Passenger p, Ride r  " +
            "where p.id=:passengerId " +
            "and " +
            "p member of r.passengers " +
            "and r.timeOfEnd<=:timeOfEnd")
    Page<Ride> findAllRidesByPassengerIdAndTimeOfEndBefore(
            Long passengerId,
            Date timeOfEnd,
            Pageable page);

    @Query("select p from Passenger p inner join Review r on p.id=r.passenger.id where r.id=:reviewId")
    Passenger findbyReviewId(Long reviewId);
}
