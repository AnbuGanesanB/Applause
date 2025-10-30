package com.example.award.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwardDistDto {

    private List<Integer> awardees;
    private int awardId;
    private int deptId;
    private int teamId;
}
