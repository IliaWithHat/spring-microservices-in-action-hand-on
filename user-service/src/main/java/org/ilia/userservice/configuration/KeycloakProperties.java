package org.ilia.userservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "keycloak")
@Configuration
@Getter
@Setter
public class KeycloakProperties {

    String serverUrl;
    String realm;
    String clientId;
    String clientSecret;
    String username;
    String password;
    String grantType;
}
