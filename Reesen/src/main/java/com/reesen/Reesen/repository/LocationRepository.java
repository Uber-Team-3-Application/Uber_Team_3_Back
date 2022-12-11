package com.reesen.Reesen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{

}
