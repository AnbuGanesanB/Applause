package com.example.employee.mapper;

import com.example.employee.dtos.NewUser;
import com.example.employee.dtos.NewUserDtoToAuthServer;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public NewUserDtoToAuthServer getNewUser(NewUser newUser){
        NewUserDtoToAuthServer user = new NewUserDtoToAuthServer();

        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmailVerified(true);
        user.setEnabled(true);
        return user;
    }
}
