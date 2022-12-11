package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    public Driver findByEmail(String email);
    public Driver findByEmailAndId(String email, Long id);
}
