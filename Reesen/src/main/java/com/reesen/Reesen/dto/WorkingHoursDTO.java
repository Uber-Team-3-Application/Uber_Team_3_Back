package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.WorkingHours;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class WorkingHoursDTO {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;


    public WorkingHoursDTO(WorkingHours workingHours){
        this.id = workingHours.getId();
        this.start = workingHours.getStartTime();
        this.end = workingHours.getEndTime();
    }

}
