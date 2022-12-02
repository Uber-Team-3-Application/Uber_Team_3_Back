package com.reesen.Reesen.model;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String lastName;

    @Column
    private String profilePicture;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private boolean isBlocked;

    @Column
    private boolean isActive;

    public User() {
    }

    public User(String name, String lastName, String profilePicture, String phoneNumber, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isActive = false;
        this.isBlocked = false;
    }

    public User(String name, String lastName, String profilePicture, String phoneNumber, String email, String password, boolean isBlocked, boolean isActive) {
        this.name = name;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isBlocked = isBlocked;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
