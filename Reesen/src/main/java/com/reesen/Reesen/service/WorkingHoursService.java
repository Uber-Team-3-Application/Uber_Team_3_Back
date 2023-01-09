package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.ChangeWorkingHoursDTO;
import com.reesen.Reesen.dto.CreateWorkingHoursDTO;
import com.reesen.Reesen.dto.WorkingHoursDTO;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.WorkingHours;
import com.reesen.Reesen.repository.WorkingHoursRepository;
import com.reesen.Reesen.service.interfaces.IWorkingHoursService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

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
    public WorkingHours createWorkingHours(CreateWorkingHoursDTO workingHoursDTO, Driver driver){
        WorkingHours workingHours = new WorkingHours();
        workingHours.setDriver(driver);
        workingHours.setStartTime(workingHoursDTO.getStart());
        workingHours.setEndTime(workingHoursDTO.getStart());
        return workingHours;
    }

    @Override
    public Optional<WorkingHours> findOne(Long id){
        return this.workingHoursRepository.findById(id);
    }

    @Override
    public WorkingHours editWorkingHours(WorkingHours workingHours, ChangeWorkingHoursDTO workingHoursDTO){
        workingHours.setId(workingHours.getId());
        workingHours.setStartTime(workingHours.getStartTime());
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

    @Override
    public String validateWorkingHours(WorkingHours workingHours, ChangeWorkingHoursDTO workingHoursDTO) {
        if(workingHoursDTO.getEnd().isAfter(LocalDateTime.now())){
            return "Working hour end is in future";
        }
        if(workingHoursDTO.getEnd().isBefore(workingHours.getStartTime())){
            return "End time is before start time.";
        }
        if(workingHours.getStartTime().equals(workingHoursDTO.getEnd())){
            return "End time is the same as start time.";
        }
        return "VALID";

    }

    @Override
    public Duration getTotalHoursWorkedInLastDay(Long driverId) {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        Set<WorkingHours> workingHours = workingHoursRepository.findAllByDriverIdAndEndTimeAfter(driverId, yesterday);
        if(workingHours.size() == 0) return Duration.ZERO;

        Duration totalDurationWorked = Duration.ZERO;
        // yesterday = 9:00
        // created working hours 10:30 - 10:30
        // existing 9:30 - 10:00
        // 8:50 -
        for(WorkingHours workingHour: workingHours){
            if(workingHour.getStartTime().equals(workingHour.getEndTime())){
                totalDurationWorked = totalDurationWorked.plus(Duration.between(workingHour.getStartTime(), LocalDateTime.now()));
                continue;
            }
            if(workingHour.getStartTime().isAfter(yesterday)){
                totalDurationWorked = totalDurationWorked.plus(Duration.between(workingHour.getStartTime(), workingHour.getEndTime()));
            }else{
                totalDurationWorked = totalDurationWorked.plus(Duration.between(yesterday, workingHour.getEndTime()));
            }
        }
        return totalDurationWorked;

    }

}
