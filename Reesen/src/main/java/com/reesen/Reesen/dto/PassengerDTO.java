package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {

	private Long id;
	@NotEmpty(message = "{required}")
	@Length(max=25, message = "{maxLength}")
    private String name;
	@NotEmpty(message = "{required}")
	@Length(max=25, message = "{maxLength}")
    private String surname;
	@NotBlank(message = "{format}")
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
    @NotNull
    private boolean isConfirmedMail;

    private double amountOfMoney;
    
	public PassengerDTO(Passenger passenger) {
		this.id = passenger.getId();
		this.name = passenger.getName();
		this.surname = passenger.getSurname();
		this.profilePicture = passenger.getProfilePicture();
		this.telephoneNumber = passenger.getTelephoneNumber();
		this.email = passenger.getEmail();
		this.address = passenger.getAddress();
		this.password = passenger.getPassword();
		this.isConfirmedMail = passenger.isConfirmedMail();
		this.amountOfMoney = passenger.getAmountOfMoney();
	}

    
}
