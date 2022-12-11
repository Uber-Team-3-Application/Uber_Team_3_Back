package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Document;

public interface IDocumentService {
    void remove(Long id);
    Document findOne(Long id);
    Document save(Document document);
}
