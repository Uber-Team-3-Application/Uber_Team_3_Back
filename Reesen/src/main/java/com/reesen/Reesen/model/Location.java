package com.reesen.Reesen.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double geographicWidth;

    @Column
    private double geographicLength;


}
