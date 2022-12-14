package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Panic;
import com.reesen.Reesen.model.User;

import java.time.LocalDateTime;
import java.util.Date;


public class PanicDTO {

    private Long id;
    private PanicUserDTO user;
    private PanicRideDTO ride;
    private LocalDateTime time;
    private String reason;

    public PanicDTO(){

    }
    public PanicDTO(Panic panic){
        this.id = panic.getId();
        this.user = new PanicUserDTO(panic.getUser());
        this.ride = new PanicRideDTO(panic.getRide());
        this.time = panic.getTimeOfPress();
        this.reason = panic.getReason();
    }

    public PanicDTO(PanicUserDTO user, PanicRideDTO ride, LocalDateTime time, String reason) {
        this.user = user;
        this.ride = ride;
        this.time = time;
        this.reason = reason;
    }

    public PanicDTO(Long id, PanicUserDTO user, PanicRideDTO ride, LocalDateTime time, String reason) {
        this.id = id;
        this.user = user;
        this.ride = ride;
        this.time = time;
        this.reason = reason;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PanicUserDTO getUser() {
        return user;
    }

    public void setUser(PanicUserDTO user) {
        this.user = user;
    }

    public PanicRideDTO getRide() {
        return ride;
    }

    public void setRide(PanicRideDTO ride) {
        this.ride = ride;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
