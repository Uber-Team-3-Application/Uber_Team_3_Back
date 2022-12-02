package com.reesen.Reesen.model;


import com.reesen.Reesen.Enums.VehicleName;
import jakarta.persistence.*;

@Entity
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private double pricePerKm;

    @Column
    private VehicleName name;

    public VehicleType() {
    }

    public VehicleType(double pricePerKm, VehicleName name) {
        this.pricePerKm = pricePerKm;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
