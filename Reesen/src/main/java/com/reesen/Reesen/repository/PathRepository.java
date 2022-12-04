package com.reesen.Reesen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reesen.Reesen.model.Path;

@Repository
public interface PathRepository extends JpaRepository<Path, Long>{

}
