package com.example.goody.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "award-service")
public interface AwardClient {

    @GetMapping("/v1/award/mypoints")
    int getTotalPoints(@RequestHeader("Authorization") String token);
}
