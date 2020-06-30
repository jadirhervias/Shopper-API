package com.shopper.shopperapi.models;

import org.springframework.data.mongodb.core.mapping.DBRef;
// import com.mongodb.lang.Nullable;
import lombok.Data;

@Data
public class ResponseShopsOrder{
	
//	@Nullable
	private double distance;
	
	@DBRef
	private Shop shop;

	public ResponseShopsOrder(double distance, Shop shop) {
		super();
		this.distance = distance;
		this.shop = shop;
	}
	
	@Override
	public String toString() {
		return String.format(
				"ResponseShopsOrder[distancia='%s',shops='%s']",
				distance, shop
		);
	}
}
