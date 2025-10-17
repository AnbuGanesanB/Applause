package com.example.employee.exception;

public class TeamException {

    public static class TeamNotFoundException extends RuntimeException{
        public TeamNotFoundException(String message){super(message);}
    }
}
