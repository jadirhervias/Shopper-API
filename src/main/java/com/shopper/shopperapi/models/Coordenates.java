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
        if (!(o instanceof Coordenates)) return false;
        Coordenates that = (Coordenates) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0;
    }
}
