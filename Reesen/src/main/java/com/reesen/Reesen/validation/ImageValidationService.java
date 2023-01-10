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
    public String validateImage(String image) {

        byte[] imageBytes;
        // is an image
        try {
            imageBytes = Base64.getDecoder().decode(image);
        }catch (IllegalArgumentException e){
            return "{image.notAnImage}";

        }
        // valid image
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if(bufferedImage==null) return "{image.notAnImage}";
        } catch (IOException e) {
            return "{image.notAnImage}";
        }

        final long MAX_SIZE = 5 * 1024 * 1024;
        if(imageBytes.length > MAX_SIZE)
            return "{image.biggerThanFiveM}";

        return null;
    }
}
