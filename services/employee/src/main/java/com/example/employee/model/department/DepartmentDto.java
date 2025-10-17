package com.example.employee.model.department;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private int departmentId;
    private String departmentName;
    private Map<String,String> managerDetails;
    private List<Map<String,String>> departmentMemberDetails;
}
