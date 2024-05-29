package org.ilia.organizationservice.configuration.token.introspector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class AuthoritiesOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final String clientId;
    private final OpaqueTokenIntrospector delegate;

    public AuthoritiesOpaqueTokenIntrospector(@Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}") String introspectionUri,
                                              @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}") String clientId,
                                              @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}") String clientSecret) {
        this.clientId = clientId;
        this.delegate = new NimbusOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
    }

    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = delegate.introspect(token);
        return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(), principal.getAttributes(), extractAuthorities(principal));
    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
        log.debug("All attributes: {}", principal.getAttributes());
        log.debug("All roles: {}", principal.getAuthorities());
        Map<String, Map<String, List<String>>> clientAndRoles = principal.getAttribute("resource_access");
        log.debug("Client attributes: {}", clientAndRoles);
        List<String> roles = clientAndRoles.get(clientId).get("roles");
        log.debug("Roles: {}", roles);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }
}
