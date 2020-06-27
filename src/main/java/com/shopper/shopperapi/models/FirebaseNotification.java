package com.shopper.shopperapi.models;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class FirebaseNotification {
    private String title;
    private String body;
    @JsonProperty("notification_type")
    private String notificationType;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
    
    
}
