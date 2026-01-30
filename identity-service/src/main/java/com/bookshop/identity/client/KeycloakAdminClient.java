package com.bookshop.identity.client;

import com.bookshop.identity.dtos.request.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakAdminClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${keycloak.admin.url:http://localhost:8080}")
    private String keycloakUrl;

    @Value("${keycloak.realm:bookshop}")
    private String realm;

    @Value("${keycloak.admin.client-id:admin-cli}")
    private String adminClientId;

    @Value("${keycloak.admin.client-secret:}")
    private String adminClientSecret;

    @Value("${keycloak.admin.username:}")
    private String adminUsername;

    @Value("${keycloak.admin.password:}")
    private String adminPassword;

    public String getAdminToken() {
        String url = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "client_id=" + adminClientId + "&grant_type=client_credentials";
        if (adminClientSecret != null && !adminClientSecret.isBlank()) {
            body += "&client_secret=" + adminClientSecret;
        }
        if (adminUsername != null && !adminUsername.isBlank()) {
            body = "client_id=" + adminClientId + "&username=" + adminUsername + "&password=" + adminPassword + "&grant_type=password";
            if (adminClientSecret != null && !adminClientSecret.isBlank()) {
                body += "&client_secret=" + adminClientSecret;
            }
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        if (response.getBody() != null && response.getBody().get("access_token") != null) {
            return (String) response.getBody().get("access_token");
        }
        throw new IllegalStateException("Failed to get Keycloak admin token");
    }

    public String createUser(RegisterRequest request) {
        String token = getAdminToken();
        String url = keycloakUrl + "/admin/realms/" + realm + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> user = Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "firstName", request.getFirstName() != null ? request.getFirstName() : "",
                "lastName", request.getLastName() != null ? request.getLastName() : "",
                "enabled", true,
                "emailVerified", false,
                "credentials", List.of(
                        Map.of(
                                "type", "password",
                                "value", request.getPassword(),
                                "temporary", false
                        )
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(user, headers);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getHeaders().getLocation() != null) {
            String path = response.getHeaders().getLocation().getPath();
            String userId = path.substring(path.lastIndexOf('/') + 1);
            return userId;
        }

        throw new IllegalStateException("Keycloak create user failed: " + response.getStatusCode());
    }

    public boolean existsByUsername(String username) {
        String token = getAdminToken();
        String url = keycloakUrl + "/admin/realms/" + realm + "/users?username=" + username;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        return response.getBody() != null && !response.getBody().isEmpty();
    }
}
