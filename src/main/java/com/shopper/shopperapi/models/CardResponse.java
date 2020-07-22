package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CardResponse {
    @JsonProperty("data")
    private List<InfoOperationCard> information;

    @Override
    public String toString() {
        return "RespuestaCard [information=" + information + "]";
    }
}
