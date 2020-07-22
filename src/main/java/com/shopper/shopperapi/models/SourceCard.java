package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SourceCard {
	
	@JsonProperty("card_number")
	private String CardNumber;

	@Override
	public String toString() {
		return "SourceCard [CardNumber=" + CardNumber + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SourceCard other = (SourceCard) obj;
		if (CardNumber == null) {
			if (other.CardNumber != null)
				return false;
		} else if (!CardNumber.equals(other.CardNumber))
			return false;
		return true;
	}
	
	
}
