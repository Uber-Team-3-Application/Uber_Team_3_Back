package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Driver.DriverEditBasicInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverEditBasicInfoRepository extends JpaRepository<DriverEditBasicInformation, Long> {


    @Query("select COUNT(u) from DriverEditBasicInformation u")
    int countTotal();
}
