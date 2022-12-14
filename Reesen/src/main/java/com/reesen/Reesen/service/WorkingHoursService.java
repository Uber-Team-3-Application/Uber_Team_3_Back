package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.WorkingHoursDTO;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.WorkingHours;
import com.reesen.Reesen.repository.WorkingHoursRepository;
import com.reesen.Reesen.service.interfaces.IWorkingHoursService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WorkingHoursService implements IWorkingHoursService {
    private final WorkingHoursRepository workingHoursRepository;

    public WorkingHoursService(WorkingHoursRepository workingHoursRepository){
        this.workingHoursRepository = workingHoursRepository;
    }

    @Override
    public WorkingHours save(WorkingHours workingHours){
        return this.workingHoursRepository.save(workingHours);
    }

    @Override
    public WorkingHours createWorkingHours(WorkingHoursDTO workingHoursDTO, Driver driver){
        WorkingHours workingHours = new WorkingHours();
        workingHours.setDriver(driver);
        workingHours.setStartTime(workingHoursDTO.getStart());
        workingHours.setEndTime(workingHoursDTO.getEnd());
        return workingHours;
    }

    @Override
    public Optional<WorkingHours> findOne(Long id){
        return this.workingHoursRepository.findById(id);
    }

    @Override
    public WorkingHours editWorkingHours(WorkingHours workingHours, WorkingHoursDTO workingHoursDTO){
        workingHours.setId(workingHours.getId());
        workingHours.setStartTime(workingHoursDTO.getStart());
        workingHours.setEndTime(workingHoursDTO.getEnd());
        return workingHours;
    }
    @Override
    public Page<WorkingHours> findAll(Long driverId, Pageable page, LocalDateTime from, LocalDateTime to){
        if(from == null && to == null)
            return this.workingHoursRepository.findAllByDriverId(driverId, page);
        if(to != null && from == null)
            return this.workingHoursRepository.findAllByDriverIdAndEndTimeBefore(driverId, to, page);
        if(to == null)
            return this.workingHoursRepository.findAllByDriverIdAndStartTimeAfter(driverId, from, page);

        return this.workingHoursRepository.findAllByDriverIdAndStartTimeAfterAndEndTimeBefore(driverId,
                                                                                                            from,
                                                                                                            to,
                                                                                                            page);

    }

}
