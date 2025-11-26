package com.example.employee.model.department;

import com.example.employee.dtos.EmpInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private int departmentId;
    private String departmentName;
    private EmpInfo managerDetails;
    private List<EmpInfo> departmentMemberDetails;
}
