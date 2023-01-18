package com.reesen.Reesen.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LoginDTO {

    @NotNull
    @NotEmpty(message = "{required}")
    private String email;

    @NotNull
    @NotEmpty(message = "{required}")
    private String password;

    public LoginDTO() {}

    public LoginDTO(String email,
                    String password) {
        this.email = email;
        this.password = password;
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
}
