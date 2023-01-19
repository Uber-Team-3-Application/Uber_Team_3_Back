package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Route;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>{

    @Query ("select location.departure from Route location where location=:route")
    Optional<Location> getDepartureByRoute(Route route);

    @Query ("select location.destination from Route location where location=:route")
    Optional<Location> getDestinationByRoute(Route route);



}
