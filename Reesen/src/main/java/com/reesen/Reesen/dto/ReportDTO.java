package com.reesen.Reesen.dto;

import lombok.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ReportDTO<T> {

    private Date date;
    private T total;

    public ReportDTO(Date date, T total) {
        this.date = date;
        this.total = total;

    }
}
