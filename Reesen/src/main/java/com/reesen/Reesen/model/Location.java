package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double geographicWidth;

    @Column
    private double geographicLength;

    public Location() {
    }

    public Location(double geographicWidth, double geographicLength) {
        this.geographicWidth = geographicWidth;
        this.geographicLength = geographicLength;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getGeographicWidth() {
        return geographicWidth;
    }

    public void setGeographicWidth(double geographicWidth) {
        this.geographicWidth = geographicWidth;
    }

    public double getGeographicLength() {
        return geographicLength;
    }

    public void setGeographicLength(double geographicLength) {
        this.geographicLength = geographicLength;
    }
}
