package com.reesen.Reesen.model.paginated;

import com.reesen.Reesen.dto.RemarkDTO;

import java.util.List;
import java.util.Set;

public class RemarkPaginated {
    int totalCount;
    Set<RemarkDTO> results;

    public RemarkPaginated(int totalCount, Set<RemarkDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setResults(Set<RemarkDTO> results) {
        this.results = results;
    }

    public Set<RemarkDTO> getResults() {
        return results;
    }
}
