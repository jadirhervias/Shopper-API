package com.shopper.shopperapi.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RespuestaCard {
	
	@JsonProperty("data")
	private List<InfoOperationCard> information;

	@Override
	public String toString() {
		return "RespuestaCard [information=" + information + "]";
	}
}
