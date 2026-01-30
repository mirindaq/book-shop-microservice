package com.bookshop.identity.services.impl;

import com.bookshop.identity.dtos.request.auth.RegisterRequest;
import com.bookshop.identity.dtos.response.auth.RegisterResponse;
import com.bookshop.identity.client.KeycloakAdminClient;
import com.bookshop.identity.client.ProfileServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements com.bookshop.identity.services.RegisterService {

    private final KeycloakAdminClient keycloakAdminClient;
    private final ProfileServiceClient profileServiceClient;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (keycloakAdminClient.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (keycloakAdminClient.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
        }

        String userId = keycloakAdminClient.createUser(request);
        try {
            profileServiceClient.createProfile(userId, request);
        } catch (Exception e) {
            log.warn("Profile creation failed for userId={}, will not rollback Keycloak user", userId, e);
            throw new IllegalStateException("Account created but profile creation failed. Please contact support.");
        }

        return RegisterResponse.builder()
                .userId(userId)
                .username(request.getUsername())
                .email(request.getEmail())
                .message("Registration successful")
                .build();
    }
}
