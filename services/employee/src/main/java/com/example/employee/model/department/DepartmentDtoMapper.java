package com.example.employee.model.department;

import com.example.employee.model.employee.Employee;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DepartmentDtoMapper {

    public DepartmentDto getDepartmentDetails(Department department){
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentId(department.getId());
        departmentDto.setDepartmentName(department.getDepartmentName());

        if(department.getManager()!=null){
            departmentDto.setManagerDetails(getManagerDetails(department.getManager()));
        }

        departmentDto.setDepartmentMemberDetails(department.getDeptMembers().stream().map(this::getDepartmentMemberDetails).collect(Collectors.toList()));

        return departmentDto;
    }

    private Map<String,String> getManagerDetails(Employee manager){
        Map<String,String> managerDetails = new HashMap<>();
        managerDetails.put("Manager Id",manager.getId().toString());
        managerDetails.put("Manager Uuid",manager.getUuid());
        managerDetails.put("Manager Name",manager.getEmpName());
        return managerDetails;
    }

    private Map<String,String> getDepartmentMemberDetails(Employee employee){
        Map<String,String> member = new HashMap<>();
        member.put("Member Id",employee.getId().toString());
        member.put("Member Uuid", employee.getUuid());
        member.put("Member Name",employee.getEmpName());
        return member;
    }
}
