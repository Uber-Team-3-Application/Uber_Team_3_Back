package com.reesen.Reesen.model;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String address;

    @Column
    private double latitude;

    @Column
    private double longitude;


    public Location(double latitude, double longitude, String address) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
