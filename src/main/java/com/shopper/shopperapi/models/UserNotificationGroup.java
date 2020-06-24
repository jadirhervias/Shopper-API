package com.shopper.shopperapi.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user_notification_groups")
public class UserNotificationGroup {
    private String notificationKey;
    private String notificationKeyName;
}
