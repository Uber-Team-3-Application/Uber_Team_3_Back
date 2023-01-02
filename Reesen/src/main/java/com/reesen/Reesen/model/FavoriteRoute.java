package com.reesen.Reesen.model;

import javax.persistence.*;

import java.io.Serializable;

@Entity
public class FavoriteRoute implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId")
    private Route route;

    public FavoriteRoute(){

    }
    public FavoriteRoute(Route route) {
        this.route = route;
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
}
