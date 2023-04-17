package com.example.oauth2.config;

import com.example.oauth2.config.filter.CookieCsrfFilter;
import com.example.oauth2.config.handler.HttpStatusReturningAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated();

        http.exceptionHandling()
                // возвращаем 401 статус для не аут пользователей, запрашивающие приватный endpoint
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        // CORS
        http.cors(); // также необходимо определить конфиг CORS

        // CSRF
        // ожидаем X-XSRF-TOKEN в хедере запроса для методов POST, PUT, PATH, DELETE, UPDATE
        // https://stackoverflow.com/a/74521360/65681
        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
        delegate.setCsrfRequestAttributeName("_csrf");
        CsrfTokenRequestHandler requestHandler = delegate::handle;

        http.csrf()
                .csrfTokenRequestHandler(requestHandler)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // set cookie XSRF
        http.addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class);

        // SESSION
        // создаем сессию всегда (вставляем куки)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        // LOGIN
        // используем POST /login для входа
        http.formLogin()
                // при неуспешной аутентификации возвращаем 401 статус
                .failureHandler(new AuthenticationEntryPointFailureHandler(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                // при успешной аутентификации возвращаем 200 статус
                .successHandler(new HttpStatusReturningAuthenticationSuccessHandler());

        // LOGOUT
        // для логоута используем POST /logout
        http.logout()
                // после логоута не делать редирект, а возвращаем 204 статус
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));

        // OAUTH2
        http.oauth2Login()
                // true - success and error
                .defaultSuccessUrl("http://localhost:3000", true);

        return http.build();
    }

/*
    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> customAuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
*/
}
