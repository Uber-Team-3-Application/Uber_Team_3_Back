package com.reesen.Reesen.model.paginated;

import com.reesen.Reesen.dto.WorkingHoursDTO;

import java.util.HashSet;
import java.util.Set;

public class WorkingHoursPaginated {

    private int totalCount;
    private Set<WorkingHoursDTO> results;

    public WorkingHoursPaginated(){

    }
    public WorkingHoursPaginated(int totalCount) {
        this.totalCount = totalCount;
        this.results = new HashSet<>();
    }

    public void addWorkingHours(WorkingHoursDTO workingHoursDTO){
        this.results.add(workingHoursDTO);
    }
    public WorkingHoursPaginated(int totalCount, Set<WorkingHoursDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<WorkingHoursDTO> getResults() {
        return results;
    }

    public void setResults(Set<WorkingHoursDTO> results) {
        this.results = results;
    }
}
