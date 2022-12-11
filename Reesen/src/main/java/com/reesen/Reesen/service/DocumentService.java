package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.DocumentDTO;
import com.reesen.Reesen.model.Document;
import com.reesen.Reesen.repository.DocumentRepository;
import com.reesen.Reesen.service.interfaces.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DocumentService implements IDocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository){
        this.documentRepository = documentRepository;
    }

    @Override
    public void remove(Long id){
        this.documentRepository.deleteById(id);
    }

    @Override
    public Optional<Document> findOne(Long id){
        return this.documentRepository.findById(id);
    }

    @Override
    public Document save(Document document) {
        return this.documentRepository.save(document);
    }

    @Override
    public void delete(Long id) {
        this.documentRepository.deleteById(id);
    }
    @Override
    public List<Document> findAllByDriverId(Long driverId){
        return this.documentRepository.findAllByDriverId(driverId);
    }

    @Override
    public Set<DocumentDTO> getDocumentDTOS(List<Document> documents){
        if(documents.size() == 0) return null;
        Set<DocumentDTO> documentDTOS = new HashSet<>();
        for(Document document : documents){
            documentDTOS.add(new DocumentDTO(document));
        }
        return documentDTOS;
    }
}
