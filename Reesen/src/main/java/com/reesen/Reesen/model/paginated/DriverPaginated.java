package com.reesen.Reesen.model.paginated;

import com.reesen.Reesen.dto.DriverDTO;

import java.util.HashSet;
import java.util.Set;

public class DriverPaginated {
    private int totalCount;
    private Set<DriverDTO> results;

    public DriverPaginated(){

    }
    public DriverPaginated(int totalCount){
        this.totalCount = totalCount;
        this.results = new HashSet<>();
    }
    public DriverPaginated(int totalCount, Set<DriverDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public void addDriver(DriverDTO driver){
        this.results.add(driver);
    }
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<DriverDTO> getResults() {
        return results;
    }

    public void setResults(Set<DriverDTO> results) {
        this.results = results;
    }
}
