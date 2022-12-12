package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.WorkingHoursDTO;
import com.reesen.Reesen.model.Driver;
import com.reesen.Reesen.model.WorkingHours;

import java.util.Optional;

public interface IWorkingHoursService {

    WorkingHours save(WorkingHours workingHours);

    WorkingHours createWorkingHours(WorkingHoursDTO workingHoursDTO, Driver driver);

    Optional<WorkingHours> findOne(Long id);

    WorkingHours editWorkingHours(WorkingHours workingHours, WorkingHoursDTO workingHoursDTO);
}
