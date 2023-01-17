package com.reesen.Reesen.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "userId")
    private Long userId;
    @Column(name = "code")
    private String code;
    @Column
    private LocalDateTime expirationDate;

    public ResetPasswordToken(Long userId) {
        this.userId = userId;
        this.code =  generateCode();
        this.expirationDate = LocalDateTime.now().plusHours(3);
    }

    private String generateCode(){
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.format("%06d", code);
    }
}
