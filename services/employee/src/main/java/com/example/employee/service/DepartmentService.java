package com.example.employee.service;

import com.example.employee.model.department.Department;
import com.example.employee.model.department.DepartmentDto;
import com.example.employee.model.department.DepartmentDtoMapper;
import com.example.employee.model.employee.Employee;
import com.example.employee.repo.DepartmentRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final EntityRetrieve entityRetrieve;
    private final DepartmentDtoMapper departmentDtoMapper;

    public Department addDepartment(String deptName){
        Department department = new Department();
        department.setDepartmentName(deptName);
        return departmentRepo.save(department);
    }

    @Transactional
    public Department updateManager(int deptId, int managerId){
        Department department = entityRetrieve.getDepartmentById(deptId);

        Employee manager = null;
        if(managerId>0){
            manager = entityRetrieve.getEmployeeById(managerId);
        }

        department.setManager(manager);
        return departmentRepo.save(department);

        //Trigger notification for Manager and dept-staffs
    }

    public DepartmentDto getDepartmentDetails(Department department){
        return departmentDtoMapper.getDepartmentDetails(department);
    }

    public List<DepartmentDto> getAllDepartmentDetails(){
        return departmentRepo.findAll().stream().map(this::getDepartmentDetails).collect(Collectors.toList());
    }

}
