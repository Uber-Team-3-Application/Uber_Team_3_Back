package com.reesen.Reesen.model.paginated;

import com.reesen.Reesen.dto.RideDTO;

import java.util.Set;

public class RidePaginated {
    private int totalCount;
    private Set<RideDTO> results;

    public RidePaginated() {
    }

    public RidePaginated(int totalCount, Set<RideDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<RideDTO> getResults() {
        return results;
    }

    public void setResults(Set<RideDTO> results) {
        this.results = results;
    }
}
