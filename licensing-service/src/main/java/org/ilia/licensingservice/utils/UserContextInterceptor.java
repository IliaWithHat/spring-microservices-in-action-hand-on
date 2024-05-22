package org.ilia.licensingservice.utils;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserContextInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        log.debug("In UserContextInterceptor");

        log.debug("All headers before adding new: {}", template.headers());

        template.header(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        template.header(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
        log.debug("All headers after adding new: {}", template.headers());

        log.debug("End UserContextInterceptor");
    }
}
