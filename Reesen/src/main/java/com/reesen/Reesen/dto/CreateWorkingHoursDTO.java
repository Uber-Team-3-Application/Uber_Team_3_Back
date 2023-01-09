package com.reesen.Reesen.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreateWorkingHoursDTO {
    private LocalDateTime start;
}
