package com.reesen.Reesen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>{

}
