package com.reesen.Reesen.model;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Panic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timeOfPress;

    @Column(nullable = false)
    private String reason;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "rideId")
    private Ride ride;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Panic() {
    }

    public Panic(LocalDateTime timeOfPress, String reason, Ride ride, User user) {
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

    public LocalDateTime getTimeOfPress() {
        return timeOfPress;
    }

    public void setTimeOfPress(LocalDateTime timeOfPress) {
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
