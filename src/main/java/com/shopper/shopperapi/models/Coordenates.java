package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Override
    public String toString() {
        return "Coordenates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordenates that = (Coordenates) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }
}
