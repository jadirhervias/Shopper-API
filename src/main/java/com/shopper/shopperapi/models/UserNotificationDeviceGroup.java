package com.shopper.shopperapi.models;

import com.mongodb.lang.Nullable;
import lombok.Data;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class UserNotificationDeviceGroup {
    @Nullable
    @JsonProperty("notification_key")
    private String notificationKey;
    @JsonProperty("notification_key_name")
    private String notificationKeyName;
    @JsonProperty("registration_ids")
    private List<String> registrationIds;
}
