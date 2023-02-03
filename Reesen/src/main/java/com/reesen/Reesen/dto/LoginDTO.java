package com.reesen.Reesen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotNull
    @NotEmpty(message = "{required}")
    private String email;

    @NotNull
    @NotEmpty(message = "{required}")
    private String password;

}
