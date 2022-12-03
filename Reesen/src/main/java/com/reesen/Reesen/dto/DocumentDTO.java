package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Document;

public class DocumentDTO {

    private Long id;
    private String name;
    private String documentImage;
    private Long driverId;

    public DocumentDTO(){

    }

    public DocumentDTO(Document document){
            this.id = document.getId();
            this.name = document.getName();
            this.documentImage = document.getDocumentImage();
            this.driverId = document.getDriver().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
}
