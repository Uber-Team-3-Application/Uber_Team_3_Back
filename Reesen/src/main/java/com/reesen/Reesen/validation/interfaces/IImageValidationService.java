package com.reesen.Reesen.validation.interfaces;

import org.springframework.http.ResponseEntity;

public interface IImageValidationService {

    ResponseEntity<String> validateImage(String image);

}
