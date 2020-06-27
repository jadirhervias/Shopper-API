package com.shopper.shopperapi.models;

import javax.validation.constraints.NotNull;

public class Charge {
	
	@NotNull
	private Integer amount;
	@NotNull
	private String currency_code;
	@NotNull
	private String description;
	@NotNull
	private String email;
	@NotNull
	private String source_id;
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSource_id() {
		return source_id;
	}
	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}
	
	
}
