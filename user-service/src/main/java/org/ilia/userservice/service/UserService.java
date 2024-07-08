package org.ilia.userservice.service;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.ilia.userservice.configuration.KeycloakProperties;
import org.ilia.userservice.controller.request.SignInRequest;
import org.ilia.userservice.controller.request.SignUpRequest;
import org.ilia.userservice.controller.response.SignInResponse;
import org.ilia.userservice.controller.response.SignUpResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.keycloak.representations.idm.CredentialRepresentation.PASSWORD;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakProperties keycloakProperties;
    private final Keycloak keycloak;

    private UsersResource usersResource;
    private ClientsResource clientsResource;
    private String clientId;

    @PostConstruct
    private void init() {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        usersResource = realmResource.users();
        clientsResource = realmResource.clients();
        clientId = clientsResource.findByClientId(keycloakProperties.getClientId()).getFirst().getId();
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        UserRepresentation userToSave = mapUserToUserRepresentation(signUpRequest);
        setCredentialsToUserRepresentation(signUpRequest.getPassword(), userToSave);

        String userId;
        try (Response response = usersResource.create(userToSave)) {
            userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        }
        addRoleToUser(userId);
        return new SignUpResponse(true);
    }

    private UserRepresentation mapUserToUserRepresentation(SignUpRequest signUpRequest) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(signUpRequest.getEmail());
        userRepresentation.setFirstName(signUpRequest.getUsername());
        userRepresentation.setLastName(signUpRequest.getUsername());
        userRepresentation.setAttributes(getAttributes(signUpRequest));
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        return userRepresentation;
    }

    private Map<String, List<String>> getAttributes(SignUpRequest signUpRequest) {
        HashMap<String, List<String>> attributes = new HashMap<>();
        attributes.put("birthDate", List.of(signUpRequest.getBirthDate().toString()));
        return attributes;
    }

    private void setCredentialsToUserRepresentation(String password, UserRepresentation userRepresentation) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(PASSWORD);
        credentialRepresentation.setValue(password);

        userRepresentation.setCredentials(List.of(credentialRepresentation));
    }

    private void addRoleToUser(String userId) {
        RoleRepresentation ostockUserRole = clientsResource.get(clientId).roles().get("ostock-user").toRepresentation();
        usersResource.get(userId).roles().clientLevel(clientId).add(List.of(ostockUserRole));
    }

    public SignInResponse login(SignInRequest signInRequest) {
        return new SignInResponse(getAccessToken(signInRequest.getEmail(), signInRequest.getPassword()));
    }

    private String getAccessToken(String email, String password) {
        Keycloak tempKeycloak = Keycloak.getInstance(
                keycloakProperties.getServerUrl(),
                keycloakProperties.getRealm(),
                email,
                password,
                keycloakProperties.getClientId(),
                keycloakProperties.getClientSecret()
        );
        String token = tempKeycloak.tokenManager().getAccessToken().getToken();
        tempKeycloak.close();
        return token;
    }
}
