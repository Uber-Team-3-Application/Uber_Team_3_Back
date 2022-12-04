package com.reesen.Reesen.dto;

import java.util.Date;

public class DeductionDTO {

    private String reason;
    private Date timeOfRejection;

    public DeductionDTO(String reason, Date timeOfRejection) {
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
    }

    public DeductionDTO() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(Date timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }
}
