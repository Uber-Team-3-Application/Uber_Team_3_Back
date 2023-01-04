package com.reesen.Reesen.dto;

import lombok.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ReportDTO {

    private Date date;
    private Long total;

    public ReportDTO(Date date, Long total) {
        this.date = date;
        this.total = total;

    }
}
