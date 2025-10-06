package com.example.employee.controller;

import com.example.employee.dtos.NewUser;
import com.example.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/public/1")
    public String getPublicHome(){
        return "Public - Reached Home !!";
    }

    @GetMapping("/admin/1")
    public String getAdminHome(){
        return "Admin - Reached Home !!";
    }

    @PreAuthorize("hasRole('manager')")
    @GetMapping("/manager/1")
    public String getManagerHome(){
        return "Manager - Reached Home !!";
    }

    @PreAuthorize("hasRole('hr')")
    @GetMapping("/users")
    public List<Map<String, Object>> getKeycloakUsers(){
        return null;/*employeeService.getAllUsers();*/
    }

    @PreAuthorize("hasRole('hr')")
    @PostMapping("/users")
    public void addNewUser(@RequestBody NewUser newUser){
        employeeService.createUser(newUser);
    }

    @PreAuthorize("hasRole('hr')")
    @GetMapping("/sync")
    public void syncUsersFromKeycloak(){
        employeeService.syncUsersFromKeycloak();
    }

    @GetMapping("/send")
    public String sendMessage(){
        employeeService.sendKafkaMessage();
        return "Message sent";
    }
}
