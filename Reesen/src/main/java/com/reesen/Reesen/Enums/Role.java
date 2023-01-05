package com.reesen.Reesen.Enums;

public enum Role {
    ADMIN, DRIVER, PASSENGER, UNREGISTERED_USER;

    public static Role GetRole(String role) {
        if(role.equalsIgnoreCase("ADMIN"))
            return ADMIN;
        if(role.equalsIgnoreCase("DRIVER"))
            return DRIVER;
        if(role.equalsIgnoreCase("PASSENGER"))
            return PASSENGER;
        return UNREGISTERED_USER;
    }
}
