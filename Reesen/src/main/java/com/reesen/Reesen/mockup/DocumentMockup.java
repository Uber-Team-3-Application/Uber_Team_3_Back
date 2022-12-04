package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.DocumentDTO;

public class DocumentMockup {

    private static DocumentDTO documentDTO;

    public static DocumentDTO getDocumentDTO(){
        documentDTO = new DocumentDTO();
        documentDTO.setId(Long.parseLong("123"));
        documentDTO.setName("Vozacka dozvola");
        documentDTO.setDocumentImage("U3dhZ2dlciByb2Nrcw=");
        documentDTO.setDriverId(Long.parseLong("10"));
        return documentDTO;
    }
}
