package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.FavoriteRide;

import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public interface FavoriteRouteRepository extends JpaRepository<FavoriteRide, Long> {

    @Query("select r from FavoriteRide r inner join Passenger p  on r member of p.favouriteRoutes " +
            "where p.id=:passengerId")
    Set<FavoriteRide> findAllByPassengerId(Long passengerId);

    @Query("select r.locations from FavoriteRide r where r.id=:id")
    LinkedHashSet<Route> getLocationsByRide(Long id);

    @Query("select r.passengers from FavoriteRide r where r.id=:id")
    Set<Passenger> findPassengerByRideId(Long id);
}
