package com.reesen.Reesen.dto;

public class DriverActivityDTO {

    private boolean isActive;


    public DriverActivityDTO(boolean isActive) {
        this.isActive = isActive;
    }
    public DriverActivityDTO() {
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
