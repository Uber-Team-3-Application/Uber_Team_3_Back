package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Route;
import lombok.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class RideLocationWithTimeDTO {
    private Long rideId;
    private Date startTime;
    private Set<Route> locations;

    public RideLocationWithTimeDTO(Long rideId, Date startTime){
        this.rideId = rideId;
        this.startTime = startTime;
        this.locations = new LinkedHashSet<>();
    }

}
