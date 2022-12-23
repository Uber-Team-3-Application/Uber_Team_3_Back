package com.reesen.Reesen.model;

import com.reesen.Reesen.Enums.Role;
import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="Admins")
public class Admin extends User implements Serializable {

    public Admin() {
    }

    @Override
    public void setBlocked(boolean blocked) {
        super.setBlocked(false);
    }

    public Admin(String name, String surname, String profilePicture, String telephoneNumber, String email, String password, boolean isBlocked, boolean isActive, String address, Role role, String jwt) {
        super(name, surname, profilePicture, telephoneNumber, email, password, isBlocked, isActive, address, role, jwt);
    }

    public Admin(String name, String surname, String profilePicture, String telephoneNumber, String email, String password) {
        super(name, surname, profilePicture, telephoneNumber, email, password);
    }

    public Admin(Long id, String name, String surname, String profilePicture, String telephoneNumber, String email, String password, boolean isBlocked, boolean isActive, String address, Role role) {
        super(id, name, surname, profilePicture, telephoneNumber, email, password, isBlocked, isActive, address, role);
    }

    public Admin(String name, String surname, String profilePicture, String telephoneNumber, String email, String password, boolean isBlocked, boolean isActive, String address, Role role) {
        super(name, surname, profilePicture, telephoneNumber, email, password, isBlocked, isActive, address, role);
    }

    public Admin(String name, String surname, String profilePicture, String telephoneNumber, String email, String password, boolean isBlocked, boolean isActive, String address) {
        super(name, surname, profilePicture, telephoneNumber, email, password, isBlocked, isActive, address);
    }
}
