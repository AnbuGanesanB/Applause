package com.example.employee.model.team;

import com.example.employee.dtos.EmpInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {

    private int teamId;
    private String teamName;
    private EmpInfo teamLeadDetails;
    private List<EmpInfo> teamMemberDetails;
}
