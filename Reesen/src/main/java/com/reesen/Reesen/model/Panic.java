package com.reesen.Reesen.model;


import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Panic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date timeOfPress;

    @Column(nullable = false)
    private String reason;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "rideId")
    private Ride ride;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


    public Panic(Date timeOfPress, String reason, Ride ride, User user) {
        this.timeOfPress = timeOfPress;
        this.reason = reason;
        this.ride = ride;
        this.user = user;
    }

}
