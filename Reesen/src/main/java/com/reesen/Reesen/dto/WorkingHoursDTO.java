package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.WorkingHours;

import java.time.LocalDateTime;
import java.util.Date;

public class WorkingHoursDTO {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    public WorkingHoursDTO(){}

    public WorkingHoursDTO(WorkingHours workingHours){
        this.id = workingHours.getId();
        this.start = workingHours.getStartTime();
        this.end = workingHours.getEndTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
