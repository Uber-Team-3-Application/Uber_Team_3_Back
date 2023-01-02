package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.DocumentDTO;

import java.util.HashSet;
import java.util.Set;

public class DocumentMockup {



    public static Set<DocumentDTO> getDocumentsDTO(Long id){
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(Long.parseLong("123"));
        documentDTO.setName("Vozacka dozvola");
        documentDTO.setDocumentImage("U3dhZ2dlciByb2Nrcw=");
        documentDTO.setDriverId(id);
        Set<DocumentDTO> documents = new HashSet<>();
        documents.add(documentDTO);
        return documents;
    }

    public static DocumentDTO getDocumentDTO(){
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(Long.parseLong("123"));
        documentDTO.setName("Vozacka dozvola");
        documentDTO.setDocumentImage("U3dhZ2dlciByb2Nrcw=");
        documentDTO.setDriverId(Long.parseLong("10"));
        return documentDTO;
    }
}
