package com.example.goody.dto;

public record EmpInfo(
        int id,
        String empName,
        String firstName,
        String lastName,
        String empUuid
) {}