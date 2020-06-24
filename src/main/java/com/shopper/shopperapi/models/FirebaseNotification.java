package com.shopper.shopperapi.models;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class FirebaseNotification {
    private String title;
    private String body;
    @JsonProperty("notification_type")
    private String notificationType;
}
