package com.example.employee.model.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private int id;
    private String empName;
    private String firstName;
    private String lastName;
    private String empUuid;
    private Map<String, String> department;
    private List<Map<String,String>> teams;
    private List<Map<String,String>> teamsLed;
    private List<Map<String,String>> departmentsManaged;
}
