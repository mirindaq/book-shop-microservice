package com.bookshop.identity.client;

import com.bookshop.identity.dtos.request.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileServiceClient {

    private final WebClient webClient;

    @Value("${profile.service.url:http://localhost:8080}")
    private String profileServiceUrl;


    public void createProfile(String userId, RegisterRequest request) {
        String url = profileServiceUrl + "/profile/api/v1/profiles";
        webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "userId", userId,
                        "username", request.getUsername(),
                        "email", request.getEmail(),
                        "firstName", request.getFirstName() != null ? request.getFirstName() : "",
                        "lastName", request.getLastName() != null ? request.getLastName() : ""
                ))
                .retrieve()
                .onStatus(
                        status -> !status.is2xxSuccessful(),
                        response -> response
                                .bodyToMono(String.class)
                                .defaultIfEmpty("Profile service error")
                                .map(msg -> new IllegalStateException(
                                        "Profile service create failed: " + response.statusCode() + " - " + msg
                                ))
                )
                .toBodilessEntity()
                .block();
    }

}
