package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
public class Driver extends User implements Serializable {

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<Document> documents;

    @OneToMany(cascade = {CascadeType.REFRESH})
    private Set<Ride> rides;

    @OneToOne(cascade = {CascadeType.ALL})
    private Vehicle vehicle;

    public Driver(){

    }

    public Driver(String name, String lastName, String profilePicture, String phoneNumber, String email, String password, Set<Document> documents, Set<Ride> rides, Vehicle vehicle) {
        super(name, lastName, profilePicture, phoneNumber, email, password);
        this.documents = documents;
        this.rides = rides;
        this.vehicle = vehicle;
    }

    public Driver(String name, String lastName, String profilePicture, String phoneNumber, String email, String password, boolean isBlocked, boolean isActive, Set<Document> documents, Set<Ride> rides, Vehicle vehicle) {
        super(name, lastName, profilePicture, phoneNumber, email, password, isBlocked, isActive);
        this.documents = documents;
        this.rides = rides;
        this.vehicle = vehicle;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Set<Ride> getRides() {
        return rides;
    }

    public void setRides(Set<Ride> rides) {
        this.rides = rides;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
