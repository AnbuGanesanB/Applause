package com.example.applause.feignClients;

import com.example.applause.dto.EmpInfo;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @GetMapping("/v1/department/{deptId}")
    Map<String, Object> getDeptDetails(@PathVariable("deptId") int deptId,@RequestHeader("Authorization") String authHeader);

    @GetMapping("/v1/teams/{teamId}")
    Map<String, Object> getTeamDetails(@PathVariable("teamId") int teamId, @RequestHeader("Authorization") String authHeader);

    @GetMapping("/v1/employees")
    List<EmpInfo> getAllEmployees(@RequestHeader("Authorization") String authHeader);

    @GetMapping("/v1/employees/id/{employeeId}")
    EmpInfo getEmployee(@PathVariable("employeeId") int employeeId, @RequestHeader("Authorization") String authHeader);
}
