package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Document;
import com.reesen.Reesen.repository.DocumentRepository;
import com.reesen.Reesen.service.interfaces.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Document findOne(Long id){
        return this.documentRepository.findById(id).orElseGet(null);
    }

    @Override
    public Document save(Document document) {
        return this.documentRepository.save(document);
    }
}
