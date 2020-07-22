package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class InfoOperationCard {

    @JsonProperty("creation_date")
    private long creationDate;

    private int amount;

    @JsonProperty("currency_code")
    private String currency;

    @JsonProperty("source")
    private SourceCard sorce;

    @JsonProperty("outcome")
    private Outcome outcome;

    @JsonProperty("fee_details")
    private FeeDetails feeDetails;

    @Override
    public String toString() {
        return "InfoOperationCard [creationDate=" + creationDate + ", amount=" + amount + ", currency=" + currency
                + ", sorce=" + sorce + ", outcome=" + outcome + ", feeDetails=" + feeDetails + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoOperationCard that = (InfoOperationCard) o;
        return creationDate == that.creationDate &&
                amount == that.amount &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(sorce, that.sorce) &&
                Objects.equals(outcome, that.outcome) &&
                Objects.equals(feeDetails, that.feeDetails);
    }
}