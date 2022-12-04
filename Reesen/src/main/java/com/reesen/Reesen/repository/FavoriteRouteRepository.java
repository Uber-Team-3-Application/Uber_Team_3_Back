package com.reesen.Reesen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.FavoriteRoute;

@Repository
public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, Long> {

}
