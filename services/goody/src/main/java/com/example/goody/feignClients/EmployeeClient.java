package com.example.goody.feignClients;

import com.example.goody.dto.EmpInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @GetMapping("/v1/employees/uuid/{empUuid}")
    EmpInfo getEmployeeDetails(@PathVariable("empUuid") String empUuid, @RequestHeader("Authorization") String authHeader);
}
