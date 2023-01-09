package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Driver.Driver;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DriverDTO {

    private Long id;

    @NotEmpty(message = "{required}")
    @Length(max=25, message = "{maxLength}")
    private String name;


    @NotEmpty(message = "{required}")
    @Length(max=25, message = "{maxLength}")
    private String surname;
    private String profilePicture;

    @NotEmpty(message = "{required}")
    @Length(max=20, message = "{maxLength}")
    private String telephoneNumber;

    @Email(message = "{format}")
    @NotEmpty(message = "{required}")
    @Length(max=40, message = "{maxLength}")
    private String email;

    @NotEmpty(message = "{required}")
    @Length(max=50, message = "{maxLength}")
    private String address;

    private String password;


    public DriverDTO(Driver driver){
        this.id = driver.getId();
        this.name = driver.getName();
        this.surname = driver.getSurname();
        this.profilePicture = driver.getProfilePicture();
        this.telephoneNumber = driver.getTelephoneNumber();
        this.email = driver.getEmail();
        this.address = driver.getAddress();
        this.password = driver.getPassword();

    }
}
