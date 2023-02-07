package com.reesen.Reesen.dto;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportSumAverageDTO {

    Map<Date, Double> result;
    double sum;
    double average;
}
