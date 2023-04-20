package com.example.oauth2.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import java.io.IOException;

public class RedirectAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private String defaultTargetUrl = "/";
    private String targetUrlParameter = "continue";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        String redirectUrl = defaultTargetUrl;

        if (savedRequest == null) {
            redirectUrl = response.encodeRedirectURL(redirectUrl);
        } else if (targetUrlParameter != null) {
            String targetUrl = getTargetUrl(savedRequest);
            if (targetUrl != null) {
                redirectUrl = targetUrl;
            }
        }
        redirectUrl = concatErrorParam(redirectUrl);
        redirectUrl = response.encodeRedirectURL(redirectUrl);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Redirecting to %s", redirectUrl));
        }
        if (savedRequest != null) {
            this.requestCache.removeRequest(request, response);
        }
        response.sendRedirect(redirectUrl);
    }

    private String getTargetUrl(SavedRequest savedRequest) {
        String[] parameterValues = savedRequest.getParameterValues(targetUrlParameter);
        if (parameterValues != null && parameterValues.length > 0) {
            return parameterValues[0];
        }
        return null;
    }

    private String concatErrorParam(String uri) {
        return uri.concat("?error=true");
    }

    public void setTargetUrlParameter(String targetUrlParameter) {
        this.targetUrlParameter = targetUrlParameter;
    }

    public void setDefaultTargetUrl(String defaultTargetUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultTargetUrl),
                "defaultTarget must start with '/' or with 'http(s)'");
        this.defaultTargetUrl = defaultTargetUrl;
    }
}
