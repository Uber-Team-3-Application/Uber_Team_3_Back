package com.reesen.Reesen.model;

import jakarta.persistence.*;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private double geographicWidth;

    @Column
    private double geographicLength;


}
