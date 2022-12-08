package com.reesen.Reesen.model.paginated;

import com.reesen.Reesen.dto.PassengerDTO;

import java.util.HashSet;
import java.util.Set;

public class PassengersPaginated {
    private int totalCount;
    private Set<PassengerDTO> results;

    public PassengersPaginated(){

    }

    public PassengersPaginated(int totalCount) {
        this.totalCount = totalCount;
        this.results = new HashSet<>();
    }

    public void addPassenger(PassengerDTO passenger){
        this.results.add(passenger);
    }
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<PassengerDTO> getResults() {
        return results;
    }

    public void setResults(Set<PassengerDTO> results) {
        this.results = results;
    }

}
