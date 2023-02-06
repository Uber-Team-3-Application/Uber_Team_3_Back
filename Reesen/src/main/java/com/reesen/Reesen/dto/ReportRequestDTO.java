package com.reesen.Reesen.dto;


import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.Enums.TypeOfReport;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDTO {

    private Long driverId;
    private Role role;
    private TypeOfReport typeOfReport;
    private Date from;
    private Date to;

    public ReportRequestDTO(String role, String typeOfReport, Date from, Date to){
        this.role = Role.GetRole(role);
        this.typeOfReport = TypeOfReport.getTypeOfReport(typeOfReport);
        this.from = from;
        this.to = to;
    }

    public ReportRequestDTO(String role, String typeOfReport, Date from, Date to, Long driverId){
        this.role = Role.GetRole(role);
        this.typeOfReport = TypeOfReport.getTypeOfReport(typeOfReport);
        this.from = from;
        this.to = to;
        this.driverId = driverId;
    }


}
