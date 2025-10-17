package com.example.employee.controller;

import static com.example.employee.ApiConstant.API_VERSION;

import com.example.employee.model.department.Department;
import com.example.employee.model.department.DepartmentDto;
import com.example.employee.model.department.DepartmentDtoMapper;
import com.example.employee.service.DepartmentService;
import com.example.employee.service.EntityRetrieve;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION)
public class DepartmentController {

    private final DepartmentService departmentService;
    private final EntityRetrieve entityRetrieve;

    @PostMapping("/department")
    public DepartmentDto addDepartment(@RequestBody Map<String, Object> requestData){
        String deptName = (String) requestData.get("departmentName");
        Department department = departmentService.addDepartment(deptName);
        return departmentService.getDepartmentDetails(department);
    }

    @PatchMapping("/department/manager")
    public DepartmentDto updateManager(@RequestBody Map<String, Object> requestData){
        int deptId = (Integer) requestData.getOrDefault("departmentId",0);
        int managerId = (Integer) requestData.getOrDefault("managerId",0);

        Department department = departmentService.updateManager(deptId,managerId);
        return departmentService.getDepartmentDetails(department);
    }

    @GetMapping("/department/{deptId}")
    public DepartmentDto getDepartment(@PathVariable("deptId") int departmentId){
        Department department = entityRetrieve.getDepartmentById(departmentId);
        return departmentService.getDepartmentDetails(department);
    }

    @GetMapping("/department")
    public List<DepartmentDto> getAllDepartments(){
        return departmentService.getAllDepartmentDetails();
    }

}
