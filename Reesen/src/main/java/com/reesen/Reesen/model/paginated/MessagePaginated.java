package com.reesen.Reesen.model.paginated;

import com.reesen.Reesen.dto.MessageDTO;

import java.util.List;
import java.util.Set;

public class MessagePaginated {
    int totalCount;
    Set<MessageDTO> results;

    public  MessagePaginated(int totalCount, Set<MessageDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<MessageDTO> getResults() {
        return results;
    }

    public void setResults(Set<MessageDTO> result) {
        this.results = result;
    }
}
