package com.shopper.shopperapi.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
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
}
