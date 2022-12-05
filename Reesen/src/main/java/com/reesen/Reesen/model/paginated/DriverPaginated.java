package com.reesen.Reesen.model.paginated;

import com.reesen.Reesen.dto.CreatedDriverDTO;

import java.util.HashSet;
import java.util.Set;

public class DriverPaginated {
    private int totalCount;
    private Set<CreatedDriverDTO> results;

    public DriverPaginated(){

    }
    public DriverPaginated(int totalCount){
        this.totalCount = totalCount;
        this.results = new HashSet<>();
    }
    public DriverPaginated(int totalCount, Set<CreatedDriverDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public void addDriver(CreatedDriverDTO driver){
        this.results.add(driver);
    }
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<CreatedDriverDTO> getResults() {
        return results;
    }

    public void setResults(Set<CreatedDriverDTO> results) {
        this.results = results;
    }
}
