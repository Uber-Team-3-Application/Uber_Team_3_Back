package com.reesen.Reesen.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VehicleLocationWithAvailabilityDTO {

    private Long id;
    private boolean isAvailable;
    private String address;
    private double latitude;
    private double longitude;

}
