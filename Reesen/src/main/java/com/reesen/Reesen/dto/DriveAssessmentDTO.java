package com.reesen.Reesen.dto;

import java.util.Set;

public class DriveAssessmentDTO {

    private Set<LocationDTO> locations;
    private VehicleTypeDTO vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public DriveAssessmentDTO() {
    }

    public DriveAssessmentDTO(Set<LocationDTO> locations, VehicleTypeDTO vehicleType, boolean babyTransport, boolean petTransport) {
        this.locations = locations;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public Set<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<LocationDTO> locations) {
        this.locations = locations;
    }

    public VehicleTypeDTO getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeDTO vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }
}
