package com.reesen.Reesen.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PanicButton {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime timeOfPress;

    @Column
    private String reason;
}
