package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.DocumentDTO;
import com.reesen.Reesen.model.Document;
import com.reesen.Reesen.model.Driver.Driver;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IDocumentService {
    void remove(Long id);
    Optional<Document> findOne(Long id);
    Document save(Document document);
    void delete(Long id);

    List<Document> findAllByDriverId(Long driverId);

    Set<DocumentDTO> getDocumentDTOS(List<Document> documents);

    Optional<Long> getDriverFromDocumentId(Long id);
}
