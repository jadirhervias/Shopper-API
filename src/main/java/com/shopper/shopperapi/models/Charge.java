package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Charge {
	@NotNull
	private Integer amount;
	@NotNull
	@JsonProperty("currency_code")
	private String currency_code;
	@NotNull
	private String description;
	@NotNull
	private String email;
	@NotNull
	@JsonProperty("source_id")
	private String source_id;
}
