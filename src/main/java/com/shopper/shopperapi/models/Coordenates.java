package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Coordenates {
    @JsonProperty("lng")
    private double latitude;
    @JsonProperty("lat")
    private double longitude;
}
