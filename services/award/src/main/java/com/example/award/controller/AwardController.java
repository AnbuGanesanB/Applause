package com.example.award.controller;

import com.example.award.model.AwardType;
import com.example.award.service.AwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.award.config.ApiConstant.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION)
public class AwardController {

    private final AwardService awardService;


    @PostMapping("/award")
    public AwardType addNewAward(@RequestBody AwardType awardType){
        return awardService.createAward(awardType);
    }

    @PutMapping("/award/{awardId}")
    public AwardType editAward(@PathVariable("awardId") int awardId, @RequestBody Map<String,Object> awardTypeData){
        return awardService.editAward(awardId,awardTypeData);
    }

    @GetMapping("/award/{awardId}")
    public AwardType getAward(@PathVariable("awardId") int awardId){
        return awardService.getAward(awardId);
    }

    @GetMapping("/award")
    public List<AwardType> getAllAwards(){
        return awardService.getAllAwards();
    }


}
