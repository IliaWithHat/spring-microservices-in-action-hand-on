package org.ilia.userservice.service;

import lombok.RequiredArgsConstructor;
import org.ilia.userservice.configuration.KeycloakProperties;
import org.ilia.userservice.entity.SignInRequest;
import org.ilia.userservice.entity.SignInResponse;
import org.ilia.userservice.entity.SignUpRequest;
import org.ilia.userservice.entity.SignUpResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakProperties keycloakProperties;
    private final RestTemplate restTemplate;

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        String accessToken = getAccessToken();
        String url = keycloakProperties.getServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("temporary", false);
        credential.put("value", signUpRequest.getPassword());

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", signUpRequest.getEmail());
        payload.put("firstName", signUpRequest.getUsername());
        payload.put("lastName", signUpRequest.getUsername());
        payload.put("attributes", Map.of("birthDate", signUpRequest.getBirthDate().toString()));
        payload.put("enabled", true);
        payload.put("emailVerified", true);
        payload.put("credentials", List.of(credential));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

        HttpStatusCode statusCode = restTemplate.postForEntity(url, requestEntity, Map.class).getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            addRoleToUser(signUpRequest, headers);
            return new SignUpResponse(true);
        } else {
            return new SignUpResponse(false);
        }
    }

    private void addRoleToUser(SignUpRequest signUpRequest, HttpHeaders headers) {
        String userId = getUserIdByEmail(signUpRequest.getEmail(), headers);

        String roleUrl = keycloakProperties.getServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/roles/ostock-user";
        ResponseEntity<Map> roleResponse = restTemplate.exchange(roleUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        String roleId = (String) roleResponse.getBody().get("id");

        String roleMappingUrl = keycloakProperties.getServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/users/" + userId + "/role-mappings/realm";
        Map<String, Object> role = new HashMap<>();
        role.put("id", roleId);
        role.put("name", "ostock-user");

        HttpEntity<List<Map<String, Object>>> roleRequestEntity = new HttpEntity<>(List.of(role), headers);
        restTemplate.postForEntity(roleMappingUrl, roleRequestEntity, Void.class);
    }

    private String getUserIdByEmail(String email, HttpHeaders headers) {
        String searchUrl = keycloakProperties.getServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/users?email=" + email;
        List<Map> users = restTemplate.exchange(searchUrl, HttpMethod.GET, new HttpEntity<>(headers), List.class).getBody();
        if (users != null && !users.isEmpty()) {
            return (String) users.getFirst().get("id");
        }
        return null;
    }

    private String getAccessToken() {
        return getAccessToken(keycloakProperties.getEmail(), keycloakProperties.getPassword());
    }

    private String getAccessToken(String email, String password) {
        String url = keycloakProperties.getServerUrl()
                     + "/realms/"
                     + keycloakProperties.getRealm()
                     + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.ALL));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.add("username", email);
        payload.add("password", password);
        payload.add("grant_type", keycloakProperties.getGrantType());
        payload.add("client_id", keycloakProperties.getClientId());
        payload.add("client_secret", keycloakProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(payload, headers);

        Map<String, Object> response = (Map<String, Object>) restTemplate.postForEntity(url, requestEntity, Map.class).getBody();
        return (String) response.get("access_token");
    }

    public SignInResponse login(SignInRequest signInRequest) {
        return new SignInResponse(getAccessToken(signInRequest.getEmail(), signInRequest.getPassword()));
    }
}
