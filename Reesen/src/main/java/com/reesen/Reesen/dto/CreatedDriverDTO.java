package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Driver.Driver;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreatedDriverDTO {

    private Long id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;


    public CreatedDriverDTO(Driver driver){
        this.id = driver.getId();
        this.name = driver.getName();
        this.surname = driver.getSurname();
        this.profilePicture = driver.getProfilePicture();
        this.telephoneNumber = driver.getTelephoneNumber();
        this.email = driver.getEmail();
        this.address = driver.getAddress();

    }

}
