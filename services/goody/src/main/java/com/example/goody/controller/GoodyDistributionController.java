package com.example.goody.controller;

import com.example.goody.dto.OrderDto;
import com.example.goody.model.GoodyDistribution;
import com.example.goody.service.GoodyDistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.goody.config.ApiConstant.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION)
public class GoodyDistributionController {

    private final GoodyDistributionService goodyDistributionService;

    @PostMapping("/goody/order")
    public GoodyDistribution newGoodyOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody OrderDto orderDto) throws InterruptedException {
        return goodyDistributionService.processNewOrder(jwt, orderDto);
    }

    @GetMapping("/goody/sync")
    public void syncDetails(@RequestHeader("Authorization") String authToken){
        goodyDistributionService.syncDetails(authToken);
    }

    @PatchMapping("/goody/deliver/{gdId}")
    public GoodyDistribution deliverGoody(@PathVariable("gdId") int gdId){
        return goodyDistributionService.deliverGoodies(gdId);
    }

    @PatchMapping("/goody/cancel/{gdId}")
    public GoodyDistribution cancelGoody(@PathVariable("gdId") int gdId){
        return goodyDistributionService.cancelGoodies(gdId);
    }

    @PatchMapping("/goody/reject/{gdId}")
    public GoodyDistribution rejectGoody(@PathVariable("gdId") int gdId){
        return goodyDistributionService.rejectGoodies(gdId);
    }

    @GetMapping("/goody/order")
    public List<GoodyDistribution> getAllOrders(){
        return goodyDistributionService.getAllDistribution();
    }

    @GetMapping("/goody/order/emp/{empId}")
    public List<GoodyDistribution> getAllOrdersOfEmp(@PathVariable("empId") int empId){
        return goodyDistributionService.getAllDistributionOfEmp(empId);
    }

    @GetMapping("/goody/order/me")
    public List<GoodyDistribution> getMyAllOrders(@AuthenticationPrincipal Jwt jwt){
        return goodyDistributionService.getAllOwnDistribution(jwt);
    }

    @GetMapping("goody/order/me/points")
    public int checkAvailableRewardPoints(@AuthenticationPrincipal Jwt jwt){
        return goodyDistributionService.getAvailableRewardPoints(jwt);
    }

}
