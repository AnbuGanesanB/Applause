package com.example.applause.feignClients;

import com.example.applause.config.EmployeeFeignConfig;
import com.example.applause.kafka.EmpInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "employee-service",configuration = EmployeeFeignConfig.class)
public interface EmployeeClient {

    @GetMapping("/v1/employees/uuid/{empUuid}")
    EmpInfo getEmployeeDetails(@PathVariable("empUuid") String empUuid);

    @GetMapping("/v1/department/{deptId}")
    Map<String, Object> getDeptDetails(@PathVariable("deptId") int deptId);

    @GetMapping("/v1/teams/{teamId}")
    Map<String, Object> getTeamDetails(@PathVariable("teamId") int teamId);
}
