package com.reesen.Reesen.model.Driver;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class DriverEditPassword implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="password")
    private String password;

    public DriverEditPassword(Long id, String password) {
        this.id = id;
        this.password = password;
    }

    public DriverEditPassword(String password) {
        this.password = password;
    }

}
