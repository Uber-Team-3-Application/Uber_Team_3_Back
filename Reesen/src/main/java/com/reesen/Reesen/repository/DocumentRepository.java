package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    public List<Document> findAllByDriverId(Long driverId);
}
