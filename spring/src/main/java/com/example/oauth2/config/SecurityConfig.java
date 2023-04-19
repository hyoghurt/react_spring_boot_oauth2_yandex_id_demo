package com.example.oauth2.config;

import com.example.oauth2.config.filter.CookieCsrfFilter;
import com.example.oauth2.config.filter.SaveRequestForOAuth2AuthenticationHandlerFilter;
import com.example.oauth2.config.handler.HttpStatusReturningAuthenticationSuccessHandler;
import com.example.oauth2.config.handler.RedirectAuthenticationFailureHandler;
import com.example.oauth2.config.handler.RedirectAuthenticationSuccessHandler;
import com.example.oauth2.propertie.LocationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LocationProperties locationProperties;

    public SecurityConfig(LocationProperties locationProperties) {
        this.locationProperties = locationProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .anyRequest().authenticated();

        // return 401 for no auth users
        http.exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        // CORS (need create config)
        http.cors();

        // CSRF
        // front should insert header X-XSRF-TOKEN for request POST, PUT, PATH, DELETE, UPDATE
        // https://stackoverflow.com/a/74521360/65681
        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
        delegate.setCsrfRequestAttributeName("_csrf");
        CsrfTokenRequestHandler requestHandler = delegate::handle;
        http.csrf()
                .csrfTokenRequestHandler(requestHandler)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // set cookie XSRF
        http.addFilterAfter(new CookieCsrfFilter(), UsernamePasswordAuthenticationFilter.class);

        // disable header Authorization: Basic
        http.httpBasic().disable();

        // SESSION
        // create session always (set cookie JSESSIONID)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        // LOGIN (POST /login)
        // fail return 401 (off redirect)
        // success return 200 (off redirect)
        http.formLogin()
                .failureHandler(new AuthenticationEntryPointFailureHandler(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .successHandler(new HttpStatusReturningAuthenticationSuccessHandler());

        // LOGOUT (POST /logout)
        // return 204 (off redirect)
        http.logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));

        // OAUTH2
        // after auth redirect defaultUrl or value from param
        http.oauth2Login()
                .failureHandler(customFailureHandler())
                .successHandler(customSuccessHandler());

        // save request if request matches /oauth2/authorization/{registrationId}
        // need for redirect after auth OAUTH2
        http.addFilterBefore(new SaveRequestForOAuth2AuthenticationHandlerFilter(), OAuth2AuthorizationRequestRedirectFilter.class);

        return http.build();
    }

    private AuthenticationFailureHandler customFailureHandler() {
        RedirectAuthenticationFailureHandler failureHandler = new RedirectAuthenticationFailureHandler();
        failureHandler.setDefaultTargetUrl(locationProperties.getFront_url()); // default url if param == null
        failureHandler.setTargetUrlParameter("continue"); // param from /oauth2/authorization/{registrationId}
        return failureHandler;
    }

    private AuthenticationSuccessHandler customSuccessHandler() {
        RedirectAuthenticationSuccessHandler successHandler = new RedirectAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl(locationProperties.getFront_url()); // default url if param == null
        successHandler.setTargetUrlParameter("continue"); // param from /oauth2/authorization/{registrationId}
        successHandler.setAlwaysUseDefaultTargetUrl(true);
        return successHandler;
    }
}
