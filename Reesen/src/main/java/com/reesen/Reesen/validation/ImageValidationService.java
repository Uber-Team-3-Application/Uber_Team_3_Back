package com.reesen.Reesen.validation;

import com.reesen.Reesen.validation.interfaces.IImageValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class ImageValidationService implements IImageValidationService {
    @Override
    public ResponseEntity<String> validateImage(String image) {

        byte[] imageBytes;
        // is an image
        try {
            imageBytes = Base64.getDecoder().decode(image);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>("{image.notAnImage}", HttpStatus.BAD_REQUEST);

        }
        // valid image
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if(bufferedImage==null) return new ResponseEntity<>("{image.notAnImage}", HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("{image.notAnImage}", HttpStatus.BAD_REQUEST);
        }

        final long MAX_SIZE = 5 * 1024 * 1024;
        if(imageBytes.length > MAX_SIZE)
            return new ResponseEntity<>("{image.biggerThanFiveM}", HttpStatus.BAD_REQUEST);

        return null;
    }
}
