package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FeeDetails {
	
	@JsonProperty("variable_fee")
	private VariableFee variableFee;

	@Override
	public String toString() {
		return "FeeDetails [variableFee=" + variableFee + "]";
	}
}
