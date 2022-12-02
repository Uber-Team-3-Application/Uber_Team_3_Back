package com.reesen.Reesen.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime dateOfCreation;

    @Column
    private Integer lifespan;

}
