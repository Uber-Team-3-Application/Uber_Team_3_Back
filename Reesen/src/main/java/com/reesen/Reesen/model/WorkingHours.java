package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
public class WorkingHours implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId")
    private Driver driver;

    public WorkingHours() {
    }

    public WorkingHours(Date startTime, Date endTime, Driver driver) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.driver = driver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
