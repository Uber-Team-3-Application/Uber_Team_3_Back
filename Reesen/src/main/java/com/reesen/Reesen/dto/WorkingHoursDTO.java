package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.WorkingHours;

import java.util.Date;

public class WorkingHoursDTO {

    private Long id;
    private Date start;
    private Date end;

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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
