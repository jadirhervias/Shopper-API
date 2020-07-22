package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VariableFee {
	@JsonProperty("currency_code")
	private String Code;
	
	@JsonProperty("commision")
	private double commision;
	
	@JsonProperty("total")
	private double total;

	@Override
	public String toString() {
		return "VariableFee [Code=" + Code + ", commision=" + commision + ", total=" + total + "]";
	}

}
