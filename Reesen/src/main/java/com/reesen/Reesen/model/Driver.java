package com.reesen.Reesen.model;

import com.reesen.Reesen.Enums.Role;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="Drivers")
public class Driver extends User implements Serializable {

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Document> documents;


    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Ride> rides;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name="vehicleId")
    private Vehicle vehicle;

    public Driver(){

    }

    public Driver(Long id, String name, String surname, String profilePicture, String telephoneNumber, String email, String password, boolean isBlocked, boolean isActive, String address, Role role, Set<Document> documents, Set<Ride> rides, Vehicle vehicle) {
        super(id, name, surname, profilePicture, telephoneNumber, email, password, isBlocked, isActive, address, role);
        this.documents = documents;
        this.rides = rides;
        this.vehicle = vehicle;
    }

    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String password, boolean isBlocked, boolean isActive, String address, Role role, Set<Document> documents, Set<Ride> rides, Vehicle vehicle) {
        super(name, surname, profilePicture, telephoneNumber, email, password, isBlocked, isActive, address, role);
        this.documents = documents;
        this.rides = rides;
        this.vehicle = vehicle;
    }

    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String password, Set<Document> documents, Set<Ride> rides, Vehicle vehicle) {
        super(name, surname, profilePicture, telephoneNumber, email, password);
        this.documents = documents;
        this.rides = rides;
        this.vehicle = vehicle;
        this.setRole(Role.DRIVER);

    }

    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String password, boolean isBlocked, boolean isActive, String address, Set<Document> documents, Set<Ride> rides, Vehicle vehicle) {
        super(name, surname, profilePicture, telephoneNumber, email, password, isBlocked, isActive, address);
        this.documents = documents;
        this.rides = rides;
        this.vehicle = vehicle;

        this.setRole(Role.DRIVER);
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
