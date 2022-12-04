package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Panic;

import java.util.HashSet;
import java.util.Set;

public class PanicTotalDTO {

    private int totalCount;
    private Set<PanicDTO> results;


    public PanicTotalDTO() {
        this.results = new HashSet<>();
    }
    public void addPanicDTO(PanicDTO panicDTO){
        this.results.add(panicDTO);
    }

    public PanicTotalDTO(int totalCount, Set<PanicDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<PanicDTO> getResults() {
        return results;
    }

    public void setResults(Set<PanicDTO> results) {
        this.results = results;
    }
}
