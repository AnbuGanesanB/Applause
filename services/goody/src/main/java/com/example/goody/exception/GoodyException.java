package com.example.goody.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GoodyException {

    public static class GoodyNotFoundException extends RuntimeException{
        public GoodyNotFoundException(String message){
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class GoodyLockTimeoutException extends RuntimeException {
        public GoodyLockTimeoutException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class NoSuchOrderDistributionException extends RuntimeException {
        public NoSuchOrderDistributionException(String message) {
            super(message);
        }
    }
}
