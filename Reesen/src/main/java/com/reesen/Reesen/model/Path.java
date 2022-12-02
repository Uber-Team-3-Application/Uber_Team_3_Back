package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Path implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId")
    private Route route;

    @Column
    private double lengthInKm;

    public Path() {
    }

    public Path(Route route, double lengthInKm) {
        this.route = route;
        this.lengthInKm = lengthInKm;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public double getLengthInKm() {
        return lengthInKm;
    }

    public void setLengthInKm(double lengthInKm) {
        this.lengthInKm = lengthInKm;
    }
}
