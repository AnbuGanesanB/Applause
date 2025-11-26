package com.example.employee.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpInfo{

    private int id;
    private String empUuid;
    private String empName;
    private String email;
    private String firstName;
    private String lastName;
}
