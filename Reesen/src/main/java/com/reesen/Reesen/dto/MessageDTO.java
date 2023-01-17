package com.reesen.Reesen.dto;

import com.reesen.Reesen.Enums.TypeOfMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    @NotNull
    @NotEmpty(message = "{required}")
    private String message;
    @NotNull
    @NotEmpty(message = "{required}")
    private TypeOfMessage type;
    @NotNull
    @NotEmpty(message = "{required}")
    private Long rideId;

}
