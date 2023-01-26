package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.FavoriteRide;
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

    @Query("select p.favouriteRoutes from Passenger p where p.id=:id")
    Set<FavoriteRide> getFavoriteRides(Long id);

    @Query("select d.password from Passenger d where d.id=:id")
    String getPasswordWithId(Long id);

    @Transactional
    @Modifying
    @Query("update Passenger p set p.isConfirmedMail=:true where p.id=:passengerId")
    void activateAccount(Long passengerId);

    @Query("select p from Passenger p where :ride member of p.rides")
    Set<Passenger> findPassengersByRidesContaining(Ride ride);


    @Query("select p from Passenger p inner join Review r on p.id=r.passenger.id where r.id=:reviewId")
    Passenger findbyReviewId(Long reviewId);

    @Query("select size(p.rides) from Passenger p where p.id=:id")
    int countTotalNumberOfRides(Long id);

    @Query("select r from Ride r, Passenger p where p.id=:id and p member of r.passengers")
    Set<Ride> getPassengerRides(Long id);

    @Query("select r from FavoriteRide r, Passenger p " +
            "where p.id=:id and r member of p.favouriteRoutes")
    Set<FavoriteRide> getPassengerFavoriteRides(Long id);
}
