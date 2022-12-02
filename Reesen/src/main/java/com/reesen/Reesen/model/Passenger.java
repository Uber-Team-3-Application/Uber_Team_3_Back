package com.reesen.Reesen.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.Set;

@Entity
public class Passenger extends User implements Serializable{

    @OneToMany(cascade = {CascadeType.REFRESH})
    private Set<Ride> rides;

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<FavouriteRoutes> favouriteRoutes;

    @Column
    private boolean isConfirmedMail;

    @Column
    private double amountOfMoney;

    public Passenger() {
    }

    public Passenger(String name, String lastName, String profilePicture, String phoneNumber, String email, String password, Set<Ride> rides, Set<FavouriteRoutes> favouriteRoutes, boolean isConfirmedMail, double amountOfMoney) {
        super(name, lastName, profilePicture, phoneNumber, email, password);
        this.rides = rides;
        this.favouriteRoutes = favouriteRoutes;
        this.isConfirmedMail = isConfirmedMail;
        this.amountOfMoney = amountOfMoney;
    }

    public Passenger(String name, String lastName, String profilePicture, String phoneNumber, String email, String password, boolean isBlocked, boolean isActive, Set<Ride> rides, Set<FavouriteRoutes> favouriteRoutes, boolean isConfirmedMail, double amountOfMoney) {
        super(name, lastName, profilePicture, phoneNumber, email, password, isBlocked, isActive);
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

    public Set<FavouriteRoutes> getFavouriteRoutes() {
        return favouriteRoutes;
    }

    public void setFavouriteRoutes(Set<FavouriteRoutes> favouriteRoutes) {
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
