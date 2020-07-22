package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Outcome {
	
	@JsonProperty("type")
	private String type;

	@JsonProperty("code")
	private String code;
	
	@JsonProperty("merchant_message")
	private String message;
	
	@JsonProperty("user_message")
	private String userMessage;

	@Override
	public String toString() {
		return "Outcome [type=" + type + ", code=" + code + ", message=" + message + ", userMessage=" + userMessage
				+ "]";
	}
	
}
