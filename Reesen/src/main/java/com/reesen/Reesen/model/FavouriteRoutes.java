package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class FavouriteRoutes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Route route;

    public FavouriteRoutes(){

    }
    public FavouriteRoutes(Route route) {
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
