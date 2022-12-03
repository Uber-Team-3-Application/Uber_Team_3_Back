package com.reesen.Reesen.service;

import com.reesen.Reesen.model.WorkingHours;
import com.reesen.Reesen.repository.WorkingHoursRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkingHoursService {
    private final WorkingHoursRepository workingHoursRepository;

    public WorkingHoursService(WorkingHoursRepository workingHoursRepository){
        this.workingHoursRepository = workingHoursRepository;
    }

    public WorkingHours save(WorkingHours workingHours){
        return this.workingHoursRepository.save(workingHours);
    }

}
