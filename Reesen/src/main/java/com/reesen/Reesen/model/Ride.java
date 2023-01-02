package com.reesen.Reesen.model;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.model.Driver.Driver;

import javax.persistence.*;

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


    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Deduction deduction;

    @Column
    private boolean isPanicPressed;

    @Column
    private boolean isBabyAccessible;

    @Column
    private boolean isPetAccessible;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private VehicleType vehicleType;

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Route> locations;

    public Ride() {
    }

    public Ride(Date timeOfStart, Date timeOfEnd, double totalPrice, Driver driver, Set<Passenger> passengers,
                double estimatedTime, Set<Review> review, RideStatus status, Deduction deduction,
                boolean isPanicPressed, boolean isBabyAccessible, boolean isPetAccessible, VehicleType vehicleType, Set<Route> locations) {
        {
            this.timeOfStart = timeOfStart;
            this.timeOfEnd = timeOfEnd;
            this.totalPrice = totalPrice;
            this.driver = driver;
            this.passengers = passengers;
            this.estimatedTime = estimatedTime;
            this.review = review;
            this.status = status;
            this.deduction = deduction;
            this.isPanicPressed = isPanicPressed;
            this.isBabyAccessible = isBabyAccessible;
            this.isPetAccessible = isPetAccessible;
            this.vehicleType = vehicleType;
            this.locations = locations;
        }


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

    public Deduction getDeduction() {
        return deduction;
    }

    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
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

    public Set<Route> getLocations() {
        return locations;
    }

    public void setLocations(Set<Route> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", timeOfStart=" + timeOfStart +
                ", timeOfEnd=" + timeOfEnd +
                ", totalPrice=" + totalPrice +
                ", driver=" + driver +
                ", passengers=" + passengers +
                ", estimatedTime=" + estimatedTime +
                ", review=" + review +
                ", status=" + status +
                ", deduction=" + deduction +
                ", isPanicPressed=" + isPanicPressed +
                ", isBabyAccessible=" + isBabyAccessible +
                ", isPetAccessible=" + isPetAccessible +
                ", vehicleType=" + vehicleType +
                ", locations=" + locations +
                '}';
    }
}
