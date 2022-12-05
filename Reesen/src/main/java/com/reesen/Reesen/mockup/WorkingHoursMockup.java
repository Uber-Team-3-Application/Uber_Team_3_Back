package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.WorkingHoursDTO;
import com.reesen.Reesen.model.paginated.WorkingHoursPaginated;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class WorkingHoursMockup {

    public static WorkingHoursPaginated getWorkingHours(){
        WorkingHoursPaginated workingHoursPaginated = new WorkingHoursPaginated(243);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = null;
        try {
            date = sdf.parse("10-10-2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        WorkingHoursDTO workingHoursDTO = new WorkingHoursDTO();
        workingHoursDTO.setId(Long.parseLong("10"));
        workingHoursDTO.setStart(date);
        workingHoursDTO.setEnd(date);
        workingHoursPaginated.addWorkingHours(workingHoursDTO);
        return workingHoursPaginated;
    }
}
