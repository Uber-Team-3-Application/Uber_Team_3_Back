package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Remark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String message;

    @OneToOne(cascade = CascadeType.REFRESH)
    private User user;

    public Remark() {
    }

    public Remark(String message, User user) {
        this.message = message;
        this.user = user;
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
