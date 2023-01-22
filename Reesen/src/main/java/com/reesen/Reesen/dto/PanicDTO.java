package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Panic;
import com.reesen.Reesen.model.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class PanicDTO {

    private Long id;
    private PanicUserDTO user;
    private PanicRideDTO ride;
    private Date time;
    private String reason;

    public PanicDTO(Panic panic){
        this.id = panic.getId();
        this.user = new PanicUserDTO(panic.getUser());
        this.ride = new PanicRideDTO(panic.getRide());
        this.time = panic.getTimeOfPress();
        this.reason = panic.getReason();
    }

    public PanicDTO(PanicUserDTO user, PanicRideDTO ride, Date time, String reason) {
        this.user = user;
        this.ride = ride;
        this.time = time;
        this.reason = reason;
    }

}
