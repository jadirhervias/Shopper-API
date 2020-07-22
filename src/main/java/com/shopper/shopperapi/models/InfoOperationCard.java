package com.shopper.shopperapi.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.repackaged.com.google.common.base.Objects;

import lombok.Data;

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
	public boolean equals(Object obj) {
		if (this == obj)return true;
		if (obj == null || getClass() != obj.getClass())return false;
		InfoOperationCard that = (InfoOperationCard) obj ;
		return Objects.equal(creationDate, that.creationDate) &&
				Objects.equal(amount, that.amount) &&
				Objects.equal(currency, that.currency) &&
				Objects.equal(sorce, that.sorce) &&
				Objects.equal(outcome, that.outcome) &&
				Objects.equal(feeDetails, that.feeDetails);
	}

	@Override
	public String toString() {
		return "InfoOperationCard [creationDate=" + creationDate + ", amount=" + amount + ", currency=" + currency
				+ ", sorce=" + sorce + ", outcome=" + outcome + ", feeDetails=" + feeDetails + "]";
	}

}
