package com.reesen.Reesen.mockup;

import com.reesen.Reesen.Enums.TypeOfMessage;
import com.reesen.Reesen.dto.MessageFullDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class MessageMockup {
    public static MessageFullDTO getMessage()  {
        MessageFullDTO messageFullDTO = new MessageFullDTO();
        messageFullDTO.setId(Long.parseLong("10"));
        messageFullDTO.setTimeOfSending(Date.from(Instant.now()));
        messageFullDTO.setSenderId(Long.parseLong("123"));
        messageFullDTO.setReceiverId(Long.parseLong("123"));
        messageFullDTO.setMessage("The driver is going on a longer route on purpose");
        messageFullDTO.setType(TypeOfMessage.RIDE);
        messageFullDTO.setRideId(Long.parseLong("123"));
        return messageFullDTO;
    }

    public static String getMessageInfo() {
        return "The passenger has requested and after that aborted the ride";
    }
}
