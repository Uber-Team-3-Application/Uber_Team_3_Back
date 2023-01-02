package com.reesen.Reesen.dto;

public class ResetPasswordDTO {
    public String password;
    public Long passengerId;

    public ResetPasswordDTO(String password, Long passengerId) {
        this.password = password;
        this.passengerId = passengerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }
}
