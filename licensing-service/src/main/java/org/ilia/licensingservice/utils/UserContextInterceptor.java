package org.ilia.licensingservice.utils;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserContextInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate template) {
        log.debug("In UserContextInterceptor");
        log.debug("All headers before adding new: {}", template.headers());

        addUserContextHeader(template);
        addOauth2TokenHeader(template);

        log.debug("All headers after adding new: {}", template.headers());
        log.debug("End UserContextInterceptor");
    }

    private void addUserContextHeader(RequestTemplate template) {
        template.header(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
    }

    private void addOauth2TokenHeader(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authentication: {}", authentication);

        if (authentication instanceof BearerTokenAuthentication bearerToken) {
            log.debug("Bearer token authentication: {}", bearerToken);
            log.debug("Bearer token header: {}", bearerToken.getToken().getTokenValue());
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, bearerToken.getToken().getTokenValue()));
        }
    }
}
