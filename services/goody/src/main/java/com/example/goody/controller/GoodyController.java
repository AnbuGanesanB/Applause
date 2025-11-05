package com.example.goody.controller;

import com.example.goody.model.Goody;
import com.example.goody.service.GoodyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.goody.config.ApiConstant.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION)
public class GoodyController {

    private final GoodyService goodyService;

    @PostMapping("/goody")
    public Goody addGoody(@RequestBody Goody goody){
        return goodyService.addGoody(goody);
    }

    @PatchMapping("/goody/{goodyId}/points/{points}")
    public Goody editGoodyPoints(@PathVariable("goodyId") int goodyId, @PathVariable("points") int points){
        return goodyService.editPoints(goodyId,points);
    }

    @PatchMapping("/goody/{goodyId}/quantity/{quantity}")
    public Goody editGoodyQuantity(@PathVariable("goodyId") int goodyId, @PathVariable("quantity") int quantity){
        return goodyService.editQuantity(goodyId,quantity);
    }

    @GetMapping("/goody/{goodyId}")
    public Goody getGoody(@PathVariable("goodyId") int goodyId){
        return goodyService.getGoodyDetails(goodyId);
    }

    @GetMapping("/goody")
    public List<Goody> getAllGoodies(){
        return goodyService.getAllGoodies();
    }

}
