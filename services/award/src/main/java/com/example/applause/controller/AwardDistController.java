package com.example.applause.controller;

import com.example.applause.dto.AwardDistDto;
import com.example.applause.model.AwardDistribution;
import com.example.applause.service.AwardDistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.applause.config.ApiConstant.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION)
public class AwardDistController {

    private final AwardDistService awardDistService;

    @PostMapping("/award/awardees")
    public List<AwardDistribution> distributeAward(@RequestBody AwardDistDto awardDistDto, @RequestHeader("Authorization") String authToken){
        return awardDistService.distributeAwards(awardDistDto,authToken);
    }

    /**
     * Used for intermediate syncing of employee data with UUID.
     * One time usage.
     */
    @GetMapping("/award/awardees/syncId")
    public void syncEmpUuid(@RequestHeader("Authorization") String authToken){
        awardDistService.syncEmpUuid(authToken);
    }

    /**
     * User gets his/her own awards list
     */
    @GetMapping("/award/myawards")
    public List<AwardDistribution> getMyAwards(@AuthenticationPrincipal Jwt jwt){
        String userUuid = jwt.getClaim("sub").toString();
        return awardDistService.getUserAwards(userUuid);
    }

    /**
     * Retrieve Any employee's award list based on employee-ID (Not UUID)
     */
    @GetMapping("/award/awardees/{empId}")
    public List<AwardDistribution> getEmpAwards(@PathVariable("empId") int empId){
        return awardDistService.getUserAwards(empId);
    }

    /**
     * returns total points of querying user
     * @param jwt
     * @return
     */
    @GetMapping("/award/mypoints")
    public int getTotalPoints(@AuthenticationPrincipal Jwt jwt){
        String userUuid = jwt.getClaim("sub").toString();
        return awardDistService.getUserPoints(userUuid);
    }

}
