package com.reesen.Reesen.dto.Passenger;

import com.reesen.Reesen.model.Passenger;

public class PassengerDTO {
	
	private Long id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;
    private String password;
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


	
	public PassengerDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isConfirmedMail() {
		return isConfirmedMail;
	}

	public void setConfirmedMail(boolean isConfirmedMail) {
		this.isConfirmedMail = isConfirmedMail;
	}

	public double getAmountOfMoney() {
		return amountOfMoney;
	}

	public void setAmountOfMoney(double amountOfMoney) {
		this.amountOfMoney = amountOfMoney;
	}
	
	
    
}
