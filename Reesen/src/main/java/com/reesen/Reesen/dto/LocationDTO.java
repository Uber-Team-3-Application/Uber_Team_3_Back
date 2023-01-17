package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Location;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class LocationDTO {

    @NotEmpty(message = "{required}")
    @Length(max=40, message = "{maxLength}")
    private String address;
    @NotNull(message = "{required}")
    @Min(value = -90, message = "{regex}")
    @Max(value = 90, message = "{regex}")
    private double latitude;
    @NotNull(message = "{required}")
    @Min(value = -180, message = "{regex}")
    @Max(value = 180, message = "{regex}")
    private double longitude;

    public LocationDTO(Location location) {

        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.address = location.getAddress();
    }

}
