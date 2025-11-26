package com.example.employee.mapper;

import com.example.employee.dtos.EmpInfo;
import com.example.employee.model.employee.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmpInfoMapper {

    public EmpInfo getEmployeeInfo(Employee employee){

        EmpInfo empInfo = new EmpInfo();

        empInfo.setId(employee.getId());
        empInfo.setEmpUuid(employee.getUuid());
        empInfo.setEmpName(employee.getEmpName());
        empInfo.setEmail(employee.getEmail());
        empInfo.setFirstName(employee.getFirstName());
        empInfo.setLastName(employee.getLastName());

        return empInfo;
    }
}
