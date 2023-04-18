package com.example.oauth2.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class SaveRequestForOAuth2AuthenticationHandlerFilter extends OncePerRequestFilter {
    private RequestCache requestCache;
    private final AntPathRequestMatcher authorizationRequestMatcher;

    public SaveRequestForOAuth2AuthenticationHandlerFilter() {
        this.requestCache = new HttpSessionRequestCache();
        this.authorizationRequestMatcher = new AntPathRequestMatcher(
                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
                        + "{registrationId}");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (this.authorizationRequestMatcher.matches(request)) {
            requestCache.saveRequest(request, response);
        }

        filterChain.doFilter(request,response);
    }
}
