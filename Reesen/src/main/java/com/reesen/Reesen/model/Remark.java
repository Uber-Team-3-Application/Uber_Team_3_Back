package com.reesen.Reesen.model;

import jakarta.persistence.*;

@Entity
public class Remark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String message;

}
