package com.reesen.Reesen.model.Driver;

import com.reesen.Reesen.model.Vehicle;
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

    @Column
    private String vtype;

    public DriverEditVehicle(Vehicle vehicle, Long driverId){
        this.driverId = driverId;
        this.vModel = vehicle.getModel();
        this.vRegistrationPlate = vehicle.getRegistrationPlate();
        this.vNumberOfSeats = vehicle.getPassengerSeats();
        this.vIsBabyAccessible = vehicle.isBabyAccessible();
        this.vIsPetAccessible = vehicle.isPetAccessible();
        this.vtype = vehicle.getType().getName().toString();

    }

    public DriverEditVehicle(Long id, Long driverId, String vModel, String vRegistrationPlate, int vNumberOfSeats, boolean vIsBabyAccessible, boolean vIsPetAccessible, String vtype) {
        this.id = id;
        this.driverId = driverId;
        this.vModel = vModel;
        this.vRegistrationPlate = vRegistrationPlate;
        this.vNumberOfSeats = vNumberOfSeats;
        this.vIsBabyAccessible = vIsBabyAccessible;
        this.vIsPetAccessible = vIsPetAccessible;
        this.vtype = vtype;
    }

    public DriverEditVehicle(Long driverId, String vModel, String vRegistrationPlate, int vNumberOfSeats, boolean vIsBabyAccessible, boolean vIsPetAccessible, String vtype) {
        this.driverId = driverId;
        this.vModel = vModel;
        this.vRegistrationPlate = vRegistrationPlate;
        this.vNumberOfSeats = vNumberOfSeats;
        this.vIsBabyAccessible = vIsBabyAccessible;
        this.vIsPetAccessible = vIsPetAccessible;
        this.vtype = vtype;
    }
}
