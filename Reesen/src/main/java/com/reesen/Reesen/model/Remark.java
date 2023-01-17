package com.reesen.Reesen.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Remark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Size(max = 1200)
    private String message;

    @Column
    private Date dateOfRemark;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Remark() {
    }

    public Remark(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public Remark(String message, Date dateOfRemark, User user) {
        this.message = message;
        this.dateOfRemark = dateOfRemark;
        this.user = user;
    }

    public Date getDateOfRemark() {
        return dateOfRemark;
    }

    public void setDateOfRemark(Date dateOfRemark) {
        this.dateOfRemark = dateOfRemark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
