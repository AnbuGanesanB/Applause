package com.example.applause.exception;

public class AwardTypeException {

    public static class AwardTypeNotFoundException extends RuntimeException{
        public AwardTypeNotFoundException(String message){
            super(message);
        }
    }
}
