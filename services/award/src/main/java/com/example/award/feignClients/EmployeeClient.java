package com.example.award.feignClients;

import com.example.award.dto.EmpInfo;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @GetMapping("/v1/department/{deptId}")
    JsonNode getDeptDetails(@PathVariable("deptId") int deptId,@RequestHeader("Authorization") String authHeader);

    @GetMapping("/v1/teams/{teamId}")
    JsonNode getTeamDetails(@PathVariable("teamId") int teamId,@RequestHeader("Authorization") String authHeader);

    @GetMapping("/v1/employees")
    List<EmpInfo> getAllEmployees(@RequestHeader("Authorization") String authHeader);

    @GetMapping("/v1/employees/{employeeId}")
    EmpInfo getEmployee(@PathVariable("employeeId") int employeeId, @RequestHeader("Authorization") String authHeader);
}
