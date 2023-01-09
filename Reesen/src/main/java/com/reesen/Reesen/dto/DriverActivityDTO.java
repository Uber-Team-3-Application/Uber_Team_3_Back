package com.reesen.Reesen.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class DriverActivityDTO {

    @NotNull
    private boolean active;

}
