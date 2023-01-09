package com.reesen.Reesen.dto;

import com.reesen.Reesen.model.Document;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DocumentDTO {

    private Long id;

    @Length(max = 30, message = "{maxLength}")
    @NotBlank
    private String name;
    private String documentImage;
    private Long driverId;


    public DocumentDTO(Document document){
            this.id = document.getId();
            this.name = document.getName();
            this.documentImage = document.getDocumentImage();
            this.driverId = document.getDriver().getId();
    }

}
