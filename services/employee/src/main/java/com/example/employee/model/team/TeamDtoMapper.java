package com.example.employee.model.team;

import com.example.employee.mapper.EmpInfoMapper;
import com.example.employee.model.employee.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeamDtoMapper {

    private final EmpInfoMapper empInfoMapper;

    public TeamDto getTeamDetails(Team team){
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamId(team.getId());
        teamDto.setTeamName(team.getTeamName());
        if(team.getTeamLead()!=null)
            teamDto.setTeamLeadDetails(empInfoMapper.getEmployeeInfo(team.getTeamLead()));

        teamDto.setTeamMemberDetails(team.getMembers().stream().map(empInfoMapper::getEmployeeInfo).collect(Collectors.toList()));

        return teamDto;
    }
}
