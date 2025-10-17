package com.example.employee.model.employee;

import com.example.employee.model.department.Department;
import com.example.employee.model.team.Team;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EmployeeDtoMapper {

    public EmployeeDto getEmployeeDetails(Employee employee){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setEmpName(employee.getEmpName());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());

        Department department = employee.getDepartment();
        if(department!=null){
            Map<String,String> deptDetails = new HashMap<>();
            deptDetails.put("Department Name",department.getDepartmentName());
            deptDetails.put("Department Id", department.getId().toString());
            deptDetails.put("reporting Manager",department.getManager().getEmpName());
            employeeDto.setDepartment(deptDetails);
        }

        employeeDto.setTeams(employee.getTeams().stream().map(this::getTeamDetails).collect(Collectors.toList()));
        employeeDto.setTeamsLed(employee.getTeamsLed().stream().map(this::getTeamsLedDetails).collect(Collectors.toList()));
        employeeDto.setDepartmentsManaged(employee.getDepartmentsManaged().stream().map(this::getDepartmentsManaged).collect(Collectors.toList()));

        return employeeDto;
    }

    private Map<String,String> getTeamDetails(Team team){
        Map<String,String> teamDetails = new HashMap<>();
        teamDetails.put("Team Name",team.getTeamName());
        teamDetails.put("Team Id", team.getId().toString());
        teamDetails.put("Team Lead",team.getTeamLead().getEmpName());
        return teamDetails;
    }

    private Map<String,String> getTeamsLedDetails(Team team){
        Map<String,String> teamDetails = new HashMap<>();
        teamDetails.put("Team Name",team.getTeamName());
        teamDetails.put("Team Id", team.getId().toString());
        return teamDetails;
    }

    private Map<String,String> getDepartmentsManaged(Department department){
        Map<String,String> deptDetails = new HashMap<>();
        deptDetails.put("Department Name",department.getDepartmentName());
        deptDetails.put("Department Id", department.getId().toString());
        return deptDetails;
    }
}
