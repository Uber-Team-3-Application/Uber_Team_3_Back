package com.reesen.Reesen.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailDTO {
    private String to;
    private String subject;
    private String message;


    public EmailDTO(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

}
