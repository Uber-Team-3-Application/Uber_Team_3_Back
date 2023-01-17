package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RideLocationDTO {

    private Long rideId;
    private Location departure;
    private Location destination;

}
