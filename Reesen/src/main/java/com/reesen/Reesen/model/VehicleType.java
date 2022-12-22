package com.reesen.Reesen.model;


import com.reesen.Reesen.Enums.VehicleName;
import javax.persistence.*;

import java.io.Serializable;

@Entity
public class VehicleType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private double pricePerKm;

    @Enumerated(EnumType.STRING)
    @Column
    private VehicleName name;

    public VehicleType() {
    }

    public VehicleType(double pricePerKm, VehicleName name) {
        this.pricePerKm = pricePerKm;
        this.name = VehicleName.getVehicleName(name.toString());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public VehicleName getName() {
        return name;
    }

    public void setName(VehicleName name) {
        this.name = name;
    }
}
