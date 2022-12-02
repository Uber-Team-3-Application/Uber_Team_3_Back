package com.reesen.Reesen.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
public class UserActivation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date dateOfCreation;

    @Column
    private Long lifespan;

    @OneToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "userId")
    private User user;

    public UserActivation() {
    }

    public UserActivation(Date dateOfCreation, Long lifespan, User user) {
        this.dateOfCreation = dateOfCreation;
        this.lifespan = lifespan;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Long getLifespan() {
        return lifespan;
    }

    public void setLifespan(Long lifespan) {
        this.lifespan = lifespan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
