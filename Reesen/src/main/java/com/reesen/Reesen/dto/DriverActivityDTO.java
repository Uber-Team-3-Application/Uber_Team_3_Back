package com.reesen.Reesen.dto;

public class DriverActivityDTO {

    private boolean active;


    public DriverActivityDTO(boolean active) {
        this.active = active;
    }
    public DriverActivityDTO() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
