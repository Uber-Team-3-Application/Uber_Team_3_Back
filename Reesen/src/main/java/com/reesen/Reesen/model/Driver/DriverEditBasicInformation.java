package com.reesen.Reesen.model.Driver;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class DriverEditBasicInformation  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driverId")
    private Long driverId;

    @Column(name = "name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name = "profilePicture")
    @Lob
    private String profilePicture;

    @Column
    private String telephoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String address;


    public DriverEditBasicInformation(Driver driver, Long driverId){

        this.driverId = driverId;
        this.name = driver.getName();
        this.surname = driver.getSurname();
        this.profilePicture = driver.getProfilePicture();
        this.telephoneNumber = driver.getTelephoneNumber();
        this.email = driver.getEmail();
        this.address = driver.getAddress();
    }
    public DriverEditBasicInformation(Long id, Long driverId, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.id = id;
        this.driverId = driverId;
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
    }

    public DriverEditBasicInformation(Long driverId, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.driverId = driverId;
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
    }
}
