package com.shopper.shopperapi.utils;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class ApiKeyTokenAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    private String principalRequestBody;

    public ApiKeyTokenAuthFilter(String principalRequestBody) {
        this.principalRequestBody = principalRequestBody;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
//        {
//            "api_key_token": "IODN2389N090f23fh91ddf001"
//        }
//        return request.getParameter(principalRequestBody);
        return request.getParameter("api_key_token");
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return null;
    }
}
