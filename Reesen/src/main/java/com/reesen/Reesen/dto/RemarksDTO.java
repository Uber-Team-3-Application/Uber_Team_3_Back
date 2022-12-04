package com.reesen.Reesen.dto;

import java.util.List;

public class RemarksDTO {
    int size;
    List<RemarkDTO> remarks;

    public RemarksDTO(int size, List<RemarkDTO> remarks) {
        this.size = size;
        this.remarks = remarks;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<RemarkDTO> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<RemarkDTO> remarks) {
        this.remarks = remarks;
    }
}
