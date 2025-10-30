package com.example.award.dto;

public record EmpInfo(
        int id,
        String empName,
        String firstName,
        String lastName,
        String empUuid
) {}