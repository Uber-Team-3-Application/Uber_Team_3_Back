package com.reesen.Reesen.model;

import com.reesen.Reesen.Enums.Role;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Admin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String profilePicture;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column
    private Role role;

    public Admin() {
    }

    public Admin(String username, String password, String profilePicture, String name, String surname, Role role) {
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public Admin(String username, String password, String profilePicture, String name, String surname) {
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.name = name;
        this.surname = surname;
        this.role = Role.ADMIN;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
