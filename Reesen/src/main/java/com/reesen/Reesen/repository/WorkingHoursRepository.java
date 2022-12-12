package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.WorkingHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {

    public Page<WorkingHours> findAllByDriverId(Long driverId, Pageable page);
    public Page<WorkingHours> findAllByDriverIdAndStartTimeAfterAndEndTimeBefore(
                                                                                 Long driverId,
                                                                                 LocalDateTime starTime,
                                                                                 LocalDateTime endTime,
                                                                                 Pageable page);

    public Page<WorkingHours> findAllByDriverIdAndStartTimeAfter(
                                                                 Long driverId,
                                                                 LocalDateTime startTime,
                                                                 Pageable page);

    public Page<WorkingHours> findAllByDriverIdAndEndTimeBefore(
                                                                 Long driverId,
                                                                 LocalDateTime endTime,
                                                                 Pageable page);
}
