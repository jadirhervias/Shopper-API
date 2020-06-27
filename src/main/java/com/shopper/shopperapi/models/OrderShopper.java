package com.shopper.shopperapi.models;

import javax.validation.constraints.NotNull;

public class OrderShopper {
	@NotNull
	private double distance;
	@NotNull
	private String notification_key;
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getNotification_key() {
		return notification_key;
	}
	public void setNotification_key(String notification_key) {
		this.notification_key = notification_key;
	}
	public OrderShopper(@NotNull double distance, @NotNull String notification_key) {
		super();
		this.distance = distance;
		this.notification_key = notification_key;
	}
}
