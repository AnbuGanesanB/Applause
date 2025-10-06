package com.example.employee.feignClients;

import com.example.employee.dtos.KeycloakTokenResponse;
import com.example.employee.dtos.NewUserDtoToAuthServer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "keycloak-client",
        url = "${keycloak.base-url}"
)
public interface KeycloakClient {

    @PostMapping(
            value = "/realms/{realm}/protocol/openid-connect/token",
            consumes = "application/x-www-form-urlencoded"
    )
    KeycloakTokenResponse getServiceToken(
            @PathVariable("realm") String realm,@RequestBody Map<String, String> formParams
    );

    @GetMapping("/admin/realms/{realm}/users")
    List<Map<String, Object>> getUsers(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable("realm") String realm
    );

    @PostMapping(
            value = "/admin/realms/{realm}/users",
            consumes = "application/json"
    )
    void addUser(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody NewUserDtoToAuthServer newUser
    );

}
