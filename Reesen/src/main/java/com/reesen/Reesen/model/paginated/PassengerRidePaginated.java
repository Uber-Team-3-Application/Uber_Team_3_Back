package com.reesen.Reesen.model.paginated;

import com.reesen.Reesen.mockup.PassengerRideMockup;

import java.util.HashSet;
import java.util.Set;

public class PassengerRidePaginated {
    private int totalCount;
    private Set<PassengerRideMockup> results;

    public PassengerRidePaginated() {
    }

    public PassengerRidePaginated(int totalCount) {
        this.totalCount = totalCount;
        this.results = new HashSet<>();
    }

    public void addPassengerRide(PassengerRideMockup ride){
        this.results.add(ride);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<PassengerRideMockup> getResults() {
        return results;
    }

    public void setResults(Set<PassengerRideMockup> results) {
        this.results = results;
    }
}
