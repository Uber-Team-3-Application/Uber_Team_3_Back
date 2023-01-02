package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Panic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanicRepository extends JpaRepository<Panic, Long> {

}
