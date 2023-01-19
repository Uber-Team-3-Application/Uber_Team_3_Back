package com.reesen.Reesen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

    @NotEmpty(message = "{required}")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?!.*[^a-zA-Z0-9@#$^+=])(.{8,15})$", message = "{regex}")
    private String newPassword;

    @NotEmpty(message = "{required}")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?!.*[^a-zA-Z0-9@#$^+=])(.{8,15})$", message = "{regex}")
    private String oldPassword;
}
