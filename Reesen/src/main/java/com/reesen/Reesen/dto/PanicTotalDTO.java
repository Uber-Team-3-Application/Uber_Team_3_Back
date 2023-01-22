package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Panic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class PanicTotalDTO {

    private int totalCount;
    private Set<PanicDTO> results;


    public PanicTotalDTO() {
        this.results = new HashSet<>();
    }
    public void addPanicDTO(PanicDTO panicDTO){
        this.results.add(panicDTO);
    }

}
