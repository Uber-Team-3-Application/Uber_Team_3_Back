package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Remark;

import java.util.Date;

public class RemarkDTO {

    private Long id;
    private Date date;
    private String message;

    public RemarkDTO(Remark remark) {
        this.id = remark.getId();
        this.message = remark.getMessage();
    }


    public RemarkDTO(String message) {
        this.message = message;
    }

    public RemarkDTO(Long id, Date date, String message) {
        this.id = id;
        this.date = date;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
