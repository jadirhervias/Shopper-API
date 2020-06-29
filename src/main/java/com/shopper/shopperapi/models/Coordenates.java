package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;

@Data
@IgnoreExtraProperties
public class Coordenates {
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lng")
    private double longitude;

    public Coordenates() {
	}
}
