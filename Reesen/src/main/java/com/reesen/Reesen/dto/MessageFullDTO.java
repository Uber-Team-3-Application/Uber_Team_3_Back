package com.reesen.Reesen.dto;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.reesen.Reesen.Enums.TypeOfMessage;
import com.reesen.Reesen.model.Message;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MessageFullDTO {

    private Long id;
    private Date timeOfSending;
    private Long senderId;
    private Long receiverId;
    private String message;
    private TypeOfMessage type;
    private Long rideId;


    public MessageFullDTO(Message message) {
        this.id = message.getId();
        this.timeOfSending = message.getTimeOfSend();
        this.senderId = message.getSender().getId();
        this.receiverId = message.getReceiver().getId();
        this.message = message.getText();
        this.type = message.getTypeOfMessage();
        this.rideId = message.getRideId();

    }


    public MessageFullDTO(Long receiverId, String message, TypeOfMessage type, Long rideId) {
        this.receiverId = receiverId;
        this.message = message;
        this.type = type;
        this.rideId = rideId;

    }

}
