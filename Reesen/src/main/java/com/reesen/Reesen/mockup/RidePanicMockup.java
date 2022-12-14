package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.dto.RideDTO;

import java.time.Instant;
import java.util.Date;

public class RidePanicMockup {
    private Long id;
    private PassengerDTO user;
    private RideDTO ride;
    private Date time;
    private String reason;
    public RidePanicMockup(){
        id = Long.parseLong("10");
        user = PassengerMockup.getPassenger();
        RideMockup rideMockup = new RideMockup();
        ride = rideMockup.getRide();
        time = Date.from(Instant.now());
        reason = "Pije";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PassengerDTO getUser() {
        return user;
    }

    public void setUser(PassengerDTO user) {
        this.user = user;
    }

    public RideDTO getRide() {
        return ride;
    }

    public void setRide(RideDTO ride) {
        this.ride = ride;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
