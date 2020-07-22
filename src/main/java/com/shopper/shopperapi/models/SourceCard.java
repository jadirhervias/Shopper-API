package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class SourceCard {
    @JsonProperty("card_number")
    private String CardNumber;

    @Override
    public String toString() {
        return "SourceCard [CardNumber=" + CardNumber + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourceCard that = (SourceCard) o;
        return Objects.equals(CardNumber, that.CardNumber);
    }
}
