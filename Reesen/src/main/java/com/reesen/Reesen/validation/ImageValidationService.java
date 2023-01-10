package com.reesen.Reesen.validation;

import com.reesen.Reesen.validation.interfaces.IImageValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;

@Service
public class ImageValidationService implements IImageValidationService {

    private final MessageSource messageSource;

    @Autowired
    public ImageValidationService(MessageSource messageSource){
        this.messageSource = messageSource;
    }
    @Override
    public String validateImage(String image) {

        String base64Text = "data:image/jpeg;base64,";
        if(!image.contains(base64Text)) return messageSource.getMessage("image.notAnImage", null, Locale.getDefault());
        image = image.replace(base64Text, "");
        byte[] imageBytes;
        try {
            imageBytes = Base64.getDecoder().decode(image);
        }catch (IllegalArgumentException e){
            return messageSource.getMessage("image.notAnImage", null, Locale.getDefault());

        }
        // valid image
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if(bufferedImage==null) {
                System.out.println("Ne  valja slika 2");
                return messageSource.getMessage("image.notAnImage", null, Locale.getDefault());
            }
        } catch (IOException e) {
            System.out.println("Ne  valja slika 3");
            return messageSource.getMessage("image.notAnImage", null, Locale.getDefault());

        }

        final long MAX_SIZE = 5 * 1024 * 1024;
        if(imageBytes.length > MAX_SIZE)
            return messageSource.getMessage("image.biggerThanFiveMb", null, Locale.getDefault());

        return null;
    }
}
