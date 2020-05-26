package com.shopper.shopperapi.utils.apiKeyToken;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "shopper.http")
public class ApiKeyTokenConfig {

    private String apiKeyTokenParameter;
    private String apiKeyTokenWeb;
    private String apiKeyTokenMobile;

    public ApiKeyTokenConfig() {
    }

    public String getApiKeyTokenPrefix() {
        return apiKeyTokenParameter;
    }

    public void setApiKeyTokenPrefix(String apiKeyTokenParameter) {
        this.apiKeyTokenParameter = apiKeyTokenParameter;
    }

    public String getApiKeyTokenWeb() {
        return apiKeyTokenWeb;
    }

    public void setApiKeyTokenWeb(String apiKeyTokenWeb) {
        this.apiKeyTokenWeb = apiKeyTokenWeb;
    }

    public String getApiKeyTokenMobile() {
        return apiKeyTokenMobile;
    }

    public void setApiKeyTokenMobile(String apiKeyTokenMobile) {
        this.apiKeyTokenMobile = apiKeyTokenMobile;
    }
}
