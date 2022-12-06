package com.reesen.Reesen.model.paginated;

import java.util.HashSet;
import java.util.Set;

public class Paginated<T> {
    private int totalCount;
    private Set<T> results;


    public Paginated(int totalCount) {
        this.totalCount = totalCount;
        this.results = new HashSet<>();
    }

    public Paginated(int totalCount, Set<T> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<T> getResults() {
        return results;
    }

    public void setResults(Set<T> results) {
        this.results = results;
    }

    public void add(T t) {
        this.results.add(t);
    }
}
