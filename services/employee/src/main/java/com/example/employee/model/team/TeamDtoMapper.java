package com.example.employee.model.team;

import com.example.employee.model.employee.Employee;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TeamDtoMapper {

    public TeamDto getTeamDetails(Team team){
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamId(team.getId());
        teamDto.setTeamName(team.getTeamName());
        if(team.getTeamLead()!=null)
            teamDto.setTeamLeadDetails(getTLDetails(team.getTeamLead()));
        teamDto.setTeamMemberDetails(team.getMembers().stream().map(this::getMemberDetails).collect(Collectors.toList()));

        return teamDto;
    }

    private Map<String,String> getTLDetails(Employee teamLead){
        Map<String,String> tlDetails = new HashMap<>();
        tlDetails.put("Team Lead Id",teamLead.getId().toString());
        tlDetails.put("Team Lead Name",teamLead.getEmpName());
        return tlDetails;
    }

    private Map<String,String> getMemberDetails(Employee member){
        Map<String,String> memberDetails = new HashMap<>();
        memberDetails.put("Member Id",member.getId().toString());
        memberDetails.put("Name",member.getEmpName());
        return memberDetails;
    }
}
