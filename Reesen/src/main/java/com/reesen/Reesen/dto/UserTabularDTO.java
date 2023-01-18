package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTabularDTO {
    private Long id;
    private String name;
    private String surname;
    private String telephoneNumber;
    private String email;
    private String address;

    public UserTabularDTO(String name, String surname, String telephoneNumber, String email, String address) {
        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;

    }

    public UserTabularDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.telephoneNumber = user.getTelephoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
    }
}
