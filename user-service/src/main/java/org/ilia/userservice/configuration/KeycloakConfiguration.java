package org.ilia.userservice.configuration;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfiguration {

    private final KeycloakProperties keycloakProperties;

    @Bean
    public Keycloak keycloak() {
        return Keycloak.getInstance(
                keycloakProperties.getServerUrl(),
                keycloakProperties.getRealm(),
                keycloakProperties.getEmail(),
                keycloakProperties.getPassword(),
                keycloakProperties.getClientId(),
                keycloakProperties.getClientSecret()
        );
    }
}
