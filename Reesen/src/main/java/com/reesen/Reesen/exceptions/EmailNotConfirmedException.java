package com.reesen.Reesen.exceptions;

public class EmailNotConfirmedException extends Exception{

    public EmailNotConfirmedException() {}

    public EmailNotConfirmedException(String message) {
        super(message);
    }
}
