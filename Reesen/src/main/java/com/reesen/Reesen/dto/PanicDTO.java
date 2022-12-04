package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.User;

import java.util.Set;

public class PanicDTO {

    private Long id;
    private User user;
    private Ride ride;

    public PanicDTO(){

    }
    public PanicDTO(Long id, User user, Ride ride) {
        this.id = id;
        this.user = user;
        this.ride = ride;
    }

    public PanicDTO(User user, Ride ride) {
        this.user = user;
        this.ride = ride;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
