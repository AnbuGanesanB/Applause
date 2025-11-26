package com.example.applause.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
@RequiredArgsConstructor
public class EmployeeFeignConfig {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Bean
    public RequestInterceptor employeeServiceRequestInterceptor() {
        return requestTemplate -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("employee-service")
                    .principal("notification-service")
                    .build();


            OAuth2AuthorizedClient client =
                    authorizedClientManager.authorize(authorizeRequest);

            String token = client.getAccessToken().getTokenValue();
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
