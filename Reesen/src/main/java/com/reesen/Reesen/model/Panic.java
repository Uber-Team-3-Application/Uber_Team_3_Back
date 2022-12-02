package com.reesen.Reesen.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Panic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date timeOfPress;

    @Column(nullable = false)
    private String reason;

    @OneToOne(cascade = {CascadeType.REFRESH})
    private Ride ride;


    @OneToOne(cascade = {CascadeType.REFRESH})
    private User user;

    public Panic() {
    }

    public Panic(Date timeOfPress, String reason, Ride ride, User user) {
        this.timeOfPress = timeOfPress;
        this.reason = reason;
        this.ride = ride;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeOfPress() {
        return timeOfPress;
    }

    public void setTimeOfPress(Date timeOfPress) {
        this.timeOfPress = timeOfPress;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
