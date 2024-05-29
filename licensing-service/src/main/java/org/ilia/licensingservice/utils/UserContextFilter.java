package org.ilia.licensingservice.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("In UserContextFilter");

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        UserContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID));
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        UserContextHolder.getContext().setOrganizationId(httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        httpServletResponse.setHeader(UserContext.USER_ID, UserContextHolder.getContext().getUserId());
        httpServletResponse.setHeader(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
        httpServletResponse.setHeader(UserContext.ORGANIZATION_ID, UserContextHolder.getContext().getOrganizationId());

        log.debug("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        log.debug("End UserContextFilter");
        filterChain.doFilter(httpServletRequest, servletResponse);
    }
}
