package com.example.employee.service;

import com.example.employee.model.team.Team;
import com.example.employee.model.employee.Employee;
import com.example.employee.model.team.TeamDto;
import com.example.employee.model.team.TeamDtoMapper;
import com.example.employee.repo.TeamRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepo teamRepo;
    private final EntityRetrieve entityRetrieve;
    private final TeamDtoMapper teamDtoMapper;

    public Team addTeam(String teamName){
        Team team = new Team();
        team.setTeamName(teamName);
        return teamRepo.save(team);
    }

    @Transactional
    public Team updateTeamLead(int teamId, int teamLeadId){
        Team team = entityRetrieve.getTeamById(teamId);

        Employee teamLead = null;
        if(teamLeadId>0){
            teamLead = entityRetrieve.getEmployeeById(teamLeadId);
        }

        /*if (!team.getMembers().contains(teamLead)) {
            team.getMembers().add(teamLead);
        }*/

        team.setTeamLead(teamLead);
        return teamRepo.save(team);

        //Trigger notification for Tl and team-members
    }

    @Transactional
    public Team addMembers(int teamId, List<Integer> newMemberIds){
        if (newMemberIds == null || newMemberIds.isEmpty()) return null;

        Team team = entityRetrieve.getTeamById(teamId);
        Set<Employee> currentMembers = team.getMembers();

        Set<Employee> newMembers = new HashSet<>();

        for (int newMemberId : new HashSet<>(newMemberIds)) {
            Employee employee = entityRetrieve.getEmployeeById(newMemberId);

            // Only add if not already present
            if (currentMembers.add(employee)) {
                newMembers.add(employee);
            }
        }

        return teamRepo.save(team);

        // Send notification for concerned members
    }

    @Transactional
    public Team removeMembers(int teamId, List<Integer> removedMemberIds) {
        if (removedMemberIds == null || removedMemberIds.isEmpty()) return null;

        Team team = entityRetrieve.getTeamById(teamId);
        Set<Employee> currentMembers = team.getMembers();

        Set<Employee> removedMembers = new HashSet<>();

        for (Integer memberId : new HashSet<>(removedMemberIds)) {
            Employee employee = entityRetrieve.getEmployeeById(memberId);

            // Only remove if actually present
            if (currentMembers.remove(employee)) {
                removedMembers.add(employee);
            }
        }

        return teamRepo.save(team);

        // Optional: notify or log removals
        /*if (!removedMembers.isEmpty()) {
            notificationService.notifyRemovedTeamMembers(team, removedMembers);
        }*/
    }

    public TeamDto getTeamDetails(Team team){
        return teamDtoMapper.getTeamDetails(team);
    }

    public List<TeamDto> getAllTeam(){
        return teamRepo.findAll().stream().map(this::getTeamDetails).collect(Collectors.toList());
    }

}
