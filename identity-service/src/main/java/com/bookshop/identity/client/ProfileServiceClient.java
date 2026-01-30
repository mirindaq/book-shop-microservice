package com.bookshop.identity.client;

import com.bookshop.identity.dtos.request.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${profile.service.url:http://localhost:8080}")
    private String profileServiceUrl;


    public void createProfile(String userId, RegisterRequest request) {
        String url = profileServiceUrl + "/profile/api/v1/profiles";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "userId", userId,
                "username", request.getUsername(),
                "email", request.getEmail(),
                "firstName", request.getFirstName() != null ? request.getFirstName() : "",
                "lastName", request.getLastName() != null ? request.getLastName() : ""
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Profile service create failed: " + response.getStatusCode());
        }
    }
}
