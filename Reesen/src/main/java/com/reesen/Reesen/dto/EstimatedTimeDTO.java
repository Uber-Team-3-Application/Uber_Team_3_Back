package com.reesen.Reesen.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstimatedTimeDTO {
    private int estimatedTimeInMinutes;
    private int estimatedCost;

    public EstimatedTimeDTO(int estimatedTimeInMinutes, int estimatedCost) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.estimatedCost = estimatedCost;
    }

}
