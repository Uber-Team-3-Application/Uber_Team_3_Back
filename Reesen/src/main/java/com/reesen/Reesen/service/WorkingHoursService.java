package com.reesen.Reesen.service;

import com.reesen.Reesen.model.WorkingHours;
import com.reesen.Reesen.repository.WorkingHoursRepository;
import com.reesen.Reesen.service.interfaces.IWorkingHoursService;
import org.springframework.stereotype.Service;

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

}
