package com.reesen.Reesen.dto;

public class ResetPasswordDTO {
    public String password;
    public Long passengerId;
    public String code;

    public ResetPasswordDTO(String password, Long passengerId, String code) {
        this.password = password;
        this.passengerId = passengerId;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
