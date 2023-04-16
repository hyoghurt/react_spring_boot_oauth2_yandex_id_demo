package com.example.oauth2.config;

import com.example.oauth2.config.handler.HttpStatusReturningAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(); // также необходимо определить конфиг CORS
        http.csrf(); // храним токены в сессии, получаем через GET /csrf (ожидаем X-CSRF-TOKEN в хедере запроса для методов POST, PUT, PATH, DELETE, UPDATE)

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS); // создаем сессию всегда (вставляем куки)

        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/csrf").permitAll()
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated();

        http.formLogin() // используем POST /login для входа
                .failureHandler(new AuthenticationEntryPointFailureHandler(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))) // при неуспешной аутентификации возвращаем 401 статус
                .successHandler(new HttpStatusReturningAuthenticationSuccessHandler()); // при успешной аутентификации возвращаем 200 статус

        http.logout() // для логоута используем POST /logout
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)); // после логоута не делать редирект, а возвращаем 204 статус

        http.exceptionHandling()
                // TODO denied handler
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)); // возвращаем 401 статус для не аут пользователей, запрашивающие приватный endpoint

        http.oauth2Login()
                .defaultSuccessUrl("http://localhost:3000", true); // true - success and error

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

/*
    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> customAuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
*/
}
