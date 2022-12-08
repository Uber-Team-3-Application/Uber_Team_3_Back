package com.reesen.Reesen.model;

import com.reesen.Reesen.Enums.RideStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class Ride implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date timeOfStart;

    @Column
    private Date timeOfEnd;

    @Column
    private double totalPrice;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Driver driver;

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Passenger> passengers;

    @Column
    private double estimatedTime;


    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Review> review;

    @Enumerated(EnumType.STRING)
    @Column
    private RideStatus status;

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Deduction> deductions;

    @Column
    private boolean isPanicPressed;

    @Column
    private boolean isBabyAccessible;

    @Column
    private boolean isPetAccessible;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private VehicleType vehicleType;

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Route> routes;

    public Ride() {
    }

    public Ride(Date timeOfStart, Date timeOfEnd, double totalPrice, Driver driver, Set<Passenger> passengers, double estimatedTime, Set<Review> review, RideStatus status, Set<Deduction> deductions, boolean isPanicPressed, boolean isBabyAccessible, boolean isPetAccessible, VehicleType vehicleType, Set<Route> routes) {
        this.timeOfStart = timeOfStart;
        this.timeOfEnd = timeOfEnd;
        this.totalPrice = totalPrice;
        this.driver = driver;
        this.passengers = passengers;
        this.estimatedTime = estimatedTime;
        this.review = review;
        this.status = status;
        this.deductions = deductions;
        this.isPanicPressed = isPanicPressed;
        this.isBabyAccessible = isBabyAccessible;
        this.isPetAccessible = isPetAccessible;
        this.vehicleType = vehicleType;
        this.routes = routes;
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<Route> routes) {
        this.routes = routes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(Date timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public Date getTimeOfEnd() {
        return timeOfEnd;
    }

    public void setTimeOfEnd(Date timeOfEnd) {
        this.timeOfEnd = timeOfEnd;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Set<Review> getReview() {
        return review;
    }

    public void setReview(Set<Review> review) {
        this.review = review;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Set<Deduction> getDeductions() {
        return deductions;
    }

    public void setDeductions(Set<Deduction> deductions) {
        this.deductions = deductions;
    }

    public boolean isPanicPressed() {
        return isPanicPressed;
    }

    public void setPanicPressed(boolean panicPressed) {
        isPanicPressed = panicPressed;
    }

    public boolean isBabyAccessible() {
        return isBabyAccessible;
    }

    public void setBabyAccessible(boolean babyAccessible) {
        isBabyAccessible = babyAccessible;
    }

    public boolean isPetAccessible() {
        return isPetAccessible;
    }

    public void setPetAccessible(boolean petAccessible) {
        isPetAccessible = petAccessible;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
