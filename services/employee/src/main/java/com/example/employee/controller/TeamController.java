package com.example.employee.controller;

import com.example.employee.model.team.Team;
import com.example.employee.model.team.TeamDto;
import com.example.employee.service.EntityRetrieve;
import com.example.employee.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.employee.ApiConstant.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION)
public class TeamController {

    private final TeamService teamService;
    private final EntityRetrieve entityRetrieve;

    @PostMapping("/teams")
    public TeamDto addTeam(@RequestBody Map<String, Object> requestData){
        String teamName = (String) requestData.get("teamName");
        Team team = teamService.addTeam(teamName);
        return teamService.getTeamDetails(team);
    }

    @PatchMapping("/teams/lead")
    public TeamDto updateTeamLead(@RequestBody Map<String, Object> requestData){
        int teamId = (Integer) requestData.getOrDefault("teamId",0);
        int teamLeadId = (Integer) requestData.getOrDefault("teamLeadId",0);

        Team team = teamService.updateTeamLead(teamId,teamLeadId);
        return teamService.getTeamDetails(team);
    }

    @PostMapping("/teams/members")
    public TeamDto addMembers(@RequestBody Map<String, Object> requestData){
        int teamId = (Integer) requestData.getOrDefault("teamId",0);
        List<Integer> members = ((List<?>) requestData.getOrDefault("members", Collections.emptyList()))
                .stream()
                .map(member -> ((Number) member).intValue())
                .collect(Collectors.toList());
        Team team = teamService.addMembers(teamId,members);
        return teamService.getTeamDetails(team);
    }

    @DeleteMapping("/teams/members")
    public TeamDto removeMembers(@RequestBody Map<String, Object> requestData){
        int teamId = (Integer) requestData.getOrDefault("teamId",0);
        List<Integer> members = ((List<?>) requestData.getOrDefault("members", Collections.emptyList()))
                .stream()
                .map(member -> ((Number) member).intValue())
                .collect(Collectors.toList());
        Team team = teamService.removeMembers(teamId,members);
        return teamService.getTeamDetails(team);
    }

    @GetMapping("/teams")
    public List<TeamDto> getAllTeams(){
        return teamService.getAllTeam();
    }

    @GetMapping("/teams/{teamId}")
    public TeamDto getTeamDetails(@PathVariable("teamId") int teamId){
        Team team = entityRetrieve.getTeamById(teamId);
        return teamService.getTeamDetails(team);
    }

}
