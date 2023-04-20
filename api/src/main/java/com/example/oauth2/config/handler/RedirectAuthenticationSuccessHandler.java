package com.example.oauth2.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

public class RedirectAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        String redirectUrl = getDefaultTargetUrl();

        if (savedRequest == null) {
            redirectUrl = response.encodeRedirectURL(redirectUrl);
        } else if (getTargetUrlParameter() != null) {
            String targetUrl = getTargetUrl(savedRequest);
            if (targetUrl != null) {
                redirectUrl = targetUrl;
            }
        }
        redirectUrl = response.encodeRedirectURL(redirectUrl);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Redirecting to %s", redirectUrl));
        }
        if (savedRequest != null) {
            this.requestCache.removeRequest(request, response);
        }
        response.sendRedirect(redirectUrl);
        clearAuthenticationAttributes(request);
    }

    private String getTargetUrl(SavedRequest savedRequest) {
        String targetUrlParameter = getTargetUrlParameter();
        String[] parameterValues = savedRequest.getParameterValues(targetUrlParameter);
        if (parameterValues != null && parameterValues.length > 0) {
            return parameterValues[0];
        }
        return null;
    }
}
