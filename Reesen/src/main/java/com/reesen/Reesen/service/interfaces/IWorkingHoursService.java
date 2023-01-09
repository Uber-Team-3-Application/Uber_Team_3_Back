package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.ChangeWorkingHoursDTO;
import com.reesen.Reesen.dto.CreateWorkingHoursDTO;
import com.reesen.Reesen.dto.WorkingHoursDTO;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.WorkingHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public interface IWorkingHoursService {

    WorkingHours save(WorkingHours workingHours);

    WorkingHours createWorkingHours(CreateWorkingHoursDTO workingHoursDTO, Driver driver);

    Optional<WorkingHours> findOne(Long id);

    WorkingHours editWorkingHours(WorkingHours workingHours, ChangeWorkingHoursDTO workingHoursDTO);

    Page<WorkingHours> findAll(Long driverId, Pageable page, LocalDateTime from, LocalDateTime to);

    String validateWorkingHours(WorkingHours workingHours, ChangeWorkingHoursDTO workingHoursDTO);

    Duration getTotalHoursWorkedInLastDay(Long driverId);
}
