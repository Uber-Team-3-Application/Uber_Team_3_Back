package com.reesen.Reesen.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreateRideDTO {

    private Set<UserDTO> passengers;
    private LinkedHashSet<RouteDTO> locations;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private LocalDateTime scheduleTime;

}
