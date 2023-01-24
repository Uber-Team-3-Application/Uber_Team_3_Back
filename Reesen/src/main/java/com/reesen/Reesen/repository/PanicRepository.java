package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Panic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PanicRepository extends JpaRepository<Panic, Long> {


    @Query("select p.user.id from Panic p where p.id=:id")
    Long getUserWhoPressedPanic(Long id);

    @Query("select p.ride.id from Panic p where p.id=:id")
    Long getRideIdFromPanic(Long id);

    @Query("select p from Panic p where  p.ride.id=:id order by p.timeOfPress desc")
    List<Panic> findLatestPanicByRideId(Long id);
}
