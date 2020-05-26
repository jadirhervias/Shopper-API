package com.shopper.shopperapi.utils.apiKeyToken;

import org.springframework.stereotype.Component;

@Component
public class ApiKeyTokenVerifier {
    private final ApiKeyTokenConfig apiKeyTokenConfig;

    public ApiKeyTokenVerifier(ApiKeyTokenConfig apiKeyTokenConfig) {
        this.apiKeyTokenConfig = apiKeyTokenConfig;
    }

    public boolean verifyApiKeyToken(String apiKeyToken) {
        if (apiKeyToken.equals(apiKeyTokenConfig.getApiKeyTokenMobile()) ||
                apiKeyToken.equals(apiKeyTokenConfig.getApiKeyTokenWeb())) {
            return true;
        } else if (apiKeyToken.isEmpty()) {
            return false;
        }
        return false;
    }
}
