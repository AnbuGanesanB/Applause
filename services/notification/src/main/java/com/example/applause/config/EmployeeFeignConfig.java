package com.example.applause.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
public class EmployeeFeignConfig {

    //private final OAuth2AuthorizedClientManager authorizedClientManager;

    OAuth2AuthorizedClientManager authorizedClientManager;

    public EmployeeFeignConfig(@Lazy OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    @Bean
    public RequestInterceptor employeeServiceRequestInterceptor() {
        return requestTemplate -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("notification-service")
                    .principal("notification-service")
                    .build();


            OAuth2AuthorizedClient client =
                    authorizedClientManager.authorize(authorizeRequest);

            String token = client.getAccessToken().getTokenValue();
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
