package com.shopper.shopperapi.utils.apiKeyToken;

//import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class ApiKeyTokenFilter extends GenericFilterBean {

    private final ApiKeyTokenVerifier apiKeyTokenVerifier;

    public ApiKeyTokenFilter(ApiKeyTokenVerifier apiKeyTokenVerifier) {
        this.apiKeyTokenVerifier = apiKeyTokenVerifier;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        try {

//            ApiKeyTokenRequest apiKeyTokenRequest = new ObjectMapper()
//                    .readValue(request.getInputStream(), ApiKeyTokenRequest.class);

            ApiKeyTokenRequest apiKeyTokenRequest = new ApiKeyTokenRequest();

            apiKeyTokenRequest.setApiKeyToken(Arrays.toString(request.getParameterMap().get("api_key_token")));

            boolean isValid = apiKeyTokenVerifier.verifyApiKeyToken(apiKeyTokenRequest.getApiKeyToken());

            if (!isValid) {
                filterChain.doFilter(request, response);
                return;
            }

            // para pasar el request y response al siguiente filter (como next() en Express)
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            logger.error("Unauthorized error. Message - {}", e);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
