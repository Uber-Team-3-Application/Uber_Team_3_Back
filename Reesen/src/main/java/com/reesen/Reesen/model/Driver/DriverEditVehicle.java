package com.reesen.Reesen.model.Driver;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class DriverEditVehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driverId")
    private Long driverId;

    @Column(nullable = false)
    private String vModel;

    @Column
    private String vRegistrationPlate;

    @Column
    private int vNumberOfSeats;

    @Column
    private boolean vIsBabyAccessible;

    @Column
    private boolean vIsPetAccessible;

    public DriverEditVehicle(Long id, Long driverId, String vModel, String vRegistrationPlate, int vNumberOfSeats, boolean vIsBabyAccessible, boolean vIsPetAccessible) {
        this.id = id;
        this.driverId = driverId;
        this.vModel = vModel;
        this.vRegistrationPlate = vRegistrationPlate;
        this.vNumberOfSeats = vNumberOfSeats;
        this.vIsBabyAccessible = vIsBabyAccessible;
        this.vIsPetAccessible = vIsPetAccessible;
    }

    public DriverEditVehicle(Long driverId, String vModel, String vRegistrationPlate, int vNumberOfSeats, boolean vIsBabyAccessible, boolean vIsPetAccessible) {
        this.driverId = driverId;
        this.vModel = vModel;
        this.vRegistrationPlate = vRegistrationPlate;
        this.vNumberOfSeats = vNumberOfSeats;
        this.vIsBabyAccessible = vIsBabyAccessible;
        this.vIsPetAccessible = vIsPetAccessible;
    }
}
