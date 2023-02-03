package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PanicUserDTO {
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;


    public PanicUserDTO(User user) {
        this.name = user.getName();
        this.surname = user.getSurname();
        this.profilePicture = user.getProfilePicture();
        this.telephoneNumber = user.getTelephoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
    }


}
