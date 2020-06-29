package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Coordenates {
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lng")
    private double longitude;

	public Coordenates() {
	}
}
