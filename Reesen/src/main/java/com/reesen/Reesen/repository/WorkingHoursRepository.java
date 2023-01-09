package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.WorkingHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {

    Page<WorkingHours> findAllByDriverId(Long driverId, Pageable page);
    Page<WorkingHours> findAllByDriverIdAndStartTimeAfterAndEndTimeBefore(
                                                                                 Long driverId,
                                                                                 LocalDateTime startTime,
                                                                                 LocalDateTime endTime,
                                                                                 Pageable page);

    Page<WorkingHours> findAllByDriverIdAndStartTimeAfter(
                                                                 Long driverId,
                                                                 LocalDateTime startTime,
                                                                 Pageable page);

    Page<WorkingHours> findAllByDriverIdAndEndTimeBefore(
                                                                 Long driverId,
                                                                 LocalDateTime endTime,
                                                                 Pageable page);

    Set<WorkingHours> findAllByDriverIdAndEndTimeAfter(
            Long driverId,
            LocalDateTime endTime);
}
