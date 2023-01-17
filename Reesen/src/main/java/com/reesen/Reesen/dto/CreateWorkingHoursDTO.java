package com.reesen.Reesen.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreateWorkingHoursDTO {

    @NotNull(message = "{required}")
    private LocalDateTime start;
}
