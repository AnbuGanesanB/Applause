package com.example.employee.controller;

import com.example.employee.dtos.NewUser;
import com.example.employee.model.employee.Employee;
import com.example.employee.model.employee.EmployeeDto;
import com.example.employee.service.EmployeeService;
import com.example.employee.service.EntityRetrieve;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.employee.ApiConstant.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION)
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EntityRetrieve entityRetrieve;

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

    @GetMapping("/send")
    public String sendMessage(){
        employeeService.sendKafkaMessage();
        return "Message sent";
    }

    /*@GetMapping("/users")
    public List<Map<String, Object>> getKeycloakUsers(){
        return null;*//*employeeService.getAllUsers();*//*
    }*/

    //****************************************************

    @PreAuthorize("hasRole('hr')")
    @GetMapping("/sync")
    public void syncUsersFromKeycloak(){
        employeeService.syncUsersFromKeycloak();
    }

    @GetMapping("/employees")
    public List<EmployeeDto> getAllEmployeeDetails(){
        return employeeService.getAllEmployeeDetails();
    }

    @GetMapping("/employees/id/{employeeId}")
    public EmployeeDto getEmployeeDetails(@PathVariable("employeeId") int employeeId){
        Employee employee = entityRetrieve.getEmployeeById(employeeId);
        return employeeService.getEmployeeDetails(employee);
    }

    @GetMapping("/employees/uuid/{empUuid}")
    public EmployeeDto getEmployeeDetails(@PathVariable("empUuid") String empUuid){
        Employee employee = employeeService.getEmployeeDetails(empUuid);
        return employeeService.getEmployeeDetails(employee);
    }

    //@PreAuthorize("hasRole('hr')")
    @PostMapping("/employees")
    public EmployeeDto addNewUser(@RequestBody NewUser newUser){
        Employee employee = employeeService.createUser(newUser);
        return employeeService.getEmployeeDetails(employee);
    }

    @PatchMapping("/employees/department")
    public EmployeeDto updateDepartment(@RequestBody Map<String, Object> requestData){
        int employeeId = (Integer) requestData.getOrDefault("employeeId",0);
        int departmentId = (Integer) requestData.getOrDefault("departmentId",0);

        Employee employee = employeeService.updateDepartment(employeeId,departmentId);
        return employeeService.getEmployeeDetails(employee);
    }

}