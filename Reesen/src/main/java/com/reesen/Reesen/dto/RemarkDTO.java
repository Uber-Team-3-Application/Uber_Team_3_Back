package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Remark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class RemarkDTO {

    private Long id;
    private Date date;
    @NotEmpty
    private String message;

    public RemarkDTO(Remark remark) {
        this.id = remark.getId();
        this.message = remark.getMessage();
        this.date = remark.getDateOfRemark();
    }


    public RemarkDTO(String message) {
        this.message = message;
    }

}
