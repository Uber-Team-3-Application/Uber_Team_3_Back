package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Document;
import com.reesen.Reesen.model.Driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findAllByDriverId(Long driverId);

    @Query("select d.driver.id from Document d where d.id=:id")
    Optional<Long> findDriverByDocumentId(Long id);
}
