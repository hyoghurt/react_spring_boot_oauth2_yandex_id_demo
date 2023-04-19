package com.example.oauth2.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.Assert;

import java.io.IOException;

public class HttpStatusReturningAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpStatus httpStatus;

    public HttpStatusReturningAuthenticationSuccessHandler() {
        this.httpStatus = HttpStatus.OK;
    }

    public HttpStatusReturningAuthenticationSuccessHandler(HttpStatus httpStatus) {
        Assert.notNull(httpStatus, "The provided HttpStatus must not be null.");
        this.httpStatus = httpStatus;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(this.httpStatus.value());
        super.clearAuthenticationAttributes(request);
    }
}
