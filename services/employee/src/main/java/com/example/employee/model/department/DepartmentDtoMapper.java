package com.example.employee.model.department;

import com.example.employee.mapper.EmpInfoMapper;
import com.example.employee.model.employee.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DepartmentDtoMapper {

    private final EmpInfoMapper empInfoMapper;

    public DepartmentDto getDepartmentDetails(Department department){
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentId(department.getId());
        departmentDto.setDepartmentName(department.getDepartmentName());

        if(department.getManager()!=null){
            departmentDto.setManagerDetails(empInfoMapper.getEmployeeInfo(department.getManager()));
        }

        departmentDto.setDepartmentMemberDetails(department.getDeptMembers().stream().map(empInfoMapper::getEmployeeInfo).collect(Collectors.toList()));

        return departmentDto;
    }

}
