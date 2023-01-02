package com.reesen.Reesen.model;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Table
@Entity
public class Deduction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "rideId")
    private Ride ride;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column
    private String reason;

    @Column
    private LocalDateTime deductionTime;

    public Deduction() {
    }

    public Deduction(Ride ride, User user, String reason, LocalDateTime deductionTime) {
        this.ride = ride;
        this.user = user;
        this.reason = reason;
        this.deductionTime = deductionTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getDeductionTime() {
        return deductionTime;
    }

    public void setDeductionTime(LocalDateTime deductionTime) {
        this.deductionTime = deductionTime;
    }
}
