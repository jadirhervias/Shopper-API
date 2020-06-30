package com.shopper.shopperapi.models;

import com.mongodb.lang.Nullable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class UserNotificationDeviceGroup {
    @Nullable
    @JsonProperty("notification_key")
    private String notificationKey;
    @JsonProperty("notification_key_name")
    private String notificationKeyName;
}
