package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int driverRating;

    @Column
    private int vehicleRating;

    @Column(nullable = true)
    private String vehicleComment;

    @Column(nullable = true)
    private String driverComment;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Passenger passenger;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Ride ride;

    public Review(){


    }

    public Review(int driverRating, int vehicleRating, String vehicleComment, String driverComment, Passenger passenger, Ride ride) {
        this.driverRating = driverRating;
        this.vehicleRating = vehicleRating;
        this.vehicleComment = vehicleComment;
        this.driverComment = driverComment;
        this.passenger = passenger;
        this.ride = ride;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(int driverRating) {
        this.driverRating = driverRating;
    }

    public int getVehicleRating() {
        return vehicleRating;
    }

    public void setVehicleRating(int vehicleRating) {
        this.vehicleRating = vehicleRating;
    }

    public String getVehicleComment() {
        return vehicleComment;
    }

    public void setVehicleComment(String vehicleComment) {
        this.vehicleComment = vehicleComment;
    }

    public String getDriverComment() {
        return driverComment;
    }

    public void setDriverComment(String driverComment) {
        this.driverComment = driverComment;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
