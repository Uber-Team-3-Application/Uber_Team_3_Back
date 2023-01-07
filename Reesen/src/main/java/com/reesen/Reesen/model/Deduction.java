package com.reesen.Reesen.model;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Deduction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "rideId")
    private Ride ride;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column
    private String reason;

    @Column
    private LocalDateTime deductionTime;

    public Deduction(Ride ride, User user, String reason, LocalDateTime deductionTime) {
        this.ride = ride;
        this.user = user;
        this.reason = reason;
        this.deductionTime = deductionTime;
    }

}
