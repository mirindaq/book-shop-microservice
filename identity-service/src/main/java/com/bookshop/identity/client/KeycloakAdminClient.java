package com.bookshop.identity.client;

import com.bookshop.identity.dtos.request.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakAdminClient {

    private final WebClient webClient;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${keycloak.admin.client-secret}")
    private String adminClientSecret;

    public String getAdminToken() {
        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("client_id", adminClientId)
                        .with("client_secret", adminClientSecret)
                        .with("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(Map.class)
                .map(m -> (String) m.get("access_token"))
                .block();
    }


    public String createUser(RegisterRequest request) {
        String token = getAdminToken();

        return webClient.post()
                .uri("/admin/realms/{realm}/users", realm)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(buildUser(request))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        String location = response.headers()
                                .asHttpHeaders()
                                .getFirst(HttpHeaders.LOCATION);

                        if (location != null) {
                            return Mono.just(location.substring(location.lastIndexOf('/') + 1));
                        }
                        return Mono.error(new IllegalStateException("Missing Location header"));
                    }
                    return response.createException().flatMap(Mono::error);
                })
                .block();
    }


    public boolean existsByUsername(String username) {
        return exists("/admin/realms/{realm}/users?username={username}",
                Map.of("realm", realm, "username", username));
    }

    public boolean existsByEmail(String email) {
        return exists("/admin/realms/{realm}/users?email={email}",
                Map.of("realm", realm, "email", email));
    }

    private boolean exists(String uri, Map<String, ?> params) {
        String token = getAdminToken();

        return Boolean.TRUE.equals(webClient.get()
                .uri(uri, params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(List.class)
                .map(list -> !list.isEmpty())
                .block());
    }


    public void deleteUser(String userId) {
        String token = getAdminToken();

        webClient.delete()
                .uri("/admin/realms/{realm}/users/{id}", realm, userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity()
                .block();
    }


    private Map<String, Object> buildUser(RegisterRequest r) {
        return Map.of(
                "username", r.getUsername(),
                "email", r.getEmail(),
                "firstName", r.getFirstName() == null ? "" : r.getFirstName(),
                "lastName", r.getLastName() == null ? "" : r.getLastName(),
                "enabled", true,
                "emailVerified", false,
                "credentials", List.of(
                        Map.of(
                                "type", "password",
                                "value", r.getPassword(),
                                "temporary", false
                        )
                )
        );
    }
}

