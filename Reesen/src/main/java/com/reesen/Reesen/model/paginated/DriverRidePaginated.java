package com.reesen.Reesen.model.paginated;

import com.reesen.Reesen.mockup.DriverRideMockup;

import java.util.HashSet;
import java.util.Set;

public class DriverRidePaginated {

    private int totalCount;
    private Set<DriverRideMockup> results;

    public DriverRidePaginated(int totalCount, Set<DriverRideMockup> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public DriverRidePaginated(int totalCount) {
        this.totalCount = totalCount;
        this.results = new HashSet<>();
    }

    public void addDriverRide(DriverRideMockup ride){
        this.results.add(ride);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<DriverRideMockup> getResults() {
        return results;
    }

    public void setResults(Set<DriverRideMockup> results) {
        this.results = results;
    }
}
