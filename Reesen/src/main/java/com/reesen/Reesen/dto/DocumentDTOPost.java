package com.reesen.Reesen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentDTOPost {

    @Length(max = 30, message = "{maxLength}")
    @NotBlank(message = "{format}")
    private String name;

    @NotBlank(message = "{format}")
    private String documentImage;
    private Long driverId;
}
