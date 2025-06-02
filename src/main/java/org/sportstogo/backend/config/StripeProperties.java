package org.sportstogo.backend.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {
    /**
     * Comes from stripe.api.key in application.properties
     */
    private String apiKey;

    /**
     * Comes from stripe.webhook.secret if you set one
     */
    private String webhookSecret;

    // getters & setters
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getWebhookSecret() { return webhookSecret; }
    public void setWebhookSecret(String webhookSecret) { this.webhookSecret = webhookSecret; }
}
