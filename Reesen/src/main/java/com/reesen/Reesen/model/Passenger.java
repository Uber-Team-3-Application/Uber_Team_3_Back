package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
public class Passenger extends User implements Serializable{

    @ManyToMany(mappedBy = "ride", cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Ride> rides;

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<FavoriteRoute> favouriteRoutes;

    @Column
    private boolean isConfirmedMail;

    @Column
    private double amountOfMoney;

    public Passenger() {
    }

    public Passenger(String name, String surname, String profilePicture, String telephoneNumber, String email, String password, Set<Ride> rides, Set<FavoriteRoute> favouriteRoutes, boolean isConfirmedMail, double amountOfMoney) {
        super(name, surname, profilePicture, telephoneNumber, email, password);
        this.rides = rides;
        this.favouriteRoutes = favouriteRoutes;
        this.isConfirmedMail = isConfirmedMail;
        this.amountOfMoney = amountOfMoney;
    }

    public Passenger(String name, String surname, String profilePicture, String telephoneNumber, String email, String password, boolean isBlocked, boolean isActive, String address, Set<Ride> rides, Set<FavoriteRoute> favouriteRoutes, boolean isConfirmedMail, double amountOfMoney) {
        super(name, surname, profilePicture, telephoneNumber, email, password, isBlocked, isActive, address);
        this.rides = rides;
        this.favouriteRoutes = favouriteRoutes;
        this.isConfirmedMail = isConfirmedMail;
        this.amountOfMoney = amountOfMoney;
    }

    public Set<Ride> getRides() {
        return rides;
    }

    public void setRides(Set<Ride> rides) {
        this.rides = rides;
    }

    public Set<FavoriteRoute> getFavouriteRoutes() {
        return favouriteRoutes;
    }

    public void setFavouriteRoutes(Set<FavoriteRoute> favouriteRoutes) {
        this.favouriteRoutes = favouriteRoutes;
    }

    public boolean isConfirmedMail() {
        return isConfirmedMail;
    }

    public void setConfirmedMail(boolean confirmedMail) {
        isConfirmedMail = confirmedMail;
    }

    public double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }
}
