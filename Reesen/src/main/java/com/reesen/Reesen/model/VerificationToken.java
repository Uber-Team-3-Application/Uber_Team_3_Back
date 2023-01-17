package com.reesen.Reesen.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "passengerId")
    private Long passengerId;
    @Column(name = "url")
    private String url;
    @Column
    private LocalDateTime expirationDate;

    public VerificationToken(Long passengerId) {
        this.passengerId = passengerId;
        this.url = UUID.randomUUID().toString();
        this.expirationDate = LocalDateTime.now().plusHours(3);
    }
}