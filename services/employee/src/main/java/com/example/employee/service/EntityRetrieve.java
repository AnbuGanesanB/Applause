package com.example.employee.service;

import com.example.employee.exception.DepartmentException;
import com.example.employee.exception.EmployeeException;
import com.example.employee.exception.TeamException;
import com.example.employee.model.department.Department;
import com.example.employee.model.team.Team;
import com.example.employee.model.employee.Employee;
import com.example.employee.repo.DepartmentRepo;
import com.example.employee.repo.EmployeeRepo;
import com.example.employee.repo.TeamRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityRetrieve {

    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final TeamRepo teamRepo;

    public Employee getEmployeeById(int id){
        return employeeRepo.findById(id).orElseThrow(()->new EmployeeException.EmpNotFoundException("Employee Not found"));
    }

    public Department getDepartmentById(int id){
        return departmentRepo.findById(id).orElseThrow(()->new DepartmentException.DepartmentNotFoundException("Dept Not found"));
    }

    public Team getTeamById(int id){
        return teamRepo.findById(id).orElseThrow(()->new TeamException.TeamNotFoundException("Team not found"));
    }
}
