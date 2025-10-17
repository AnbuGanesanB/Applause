package com.example.employee.model.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {

    private int teamId;
    private String teamName;
    private Map<String,String> teamLeadDetails;
    private List<Map<String,String>> teamMemberDetails;
}
